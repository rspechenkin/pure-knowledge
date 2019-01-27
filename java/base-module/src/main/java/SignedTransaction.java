import com.test.SignedTransactions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import java.math.BigInteger;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/*
     An example of using web3j library for singing transactions off-chain and deploying a contract that verifies the signature.
     Sender wants to send some ether to Receiver.
     Sender sings a message with their private key and sends this message to the Receiver off-chain (for example an email)
     Sender deploys a contract that allows Receiver to withdraw funds. The contract also validates the signature
 */

public class SignedTransaction {

    private static final Logger LOG = LoggerFactory.getLogger(SignedTransaction.class);
    private static Properties properties = new Properties();

    public static void main(String[] args) throws Exception {
        properties.load(SignedTransaction.class.getClassLoader().getResourceAsStream("application.properties"));
        Web3j web3j = Web3j.build(new HttpService(properties.getProperty("endpoint")));
        LOG.info("Connected to Ethereum client version: " + web3j.web3ClientVersion().send().getWeb3ClientVersion());
        Credentials senderCredentials = WalletUtils.loadCredentials(
                properties.getProperty("sender.wallet.password"),
                properties.getProperty("sender.wallet.path"));
        Credentials receiverCredentials = WalletUtils.loadCredentials(
                properties.getProperty("receiver.wallet.password"),
                properties.getProperty("receiver.wallet.path"));
        LOG.info("Ethereum wallet loaded");
        printBalance(web3j, senderCredentials, receiverCredentials);
        Integer amount = 100;
        LOG.info("Deploying smart contract");
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        SignedTransactions senderContract = SignedTransactions.deploy(web3j, senderCredentials, contractGasProvider, BigInteger.valueOf(amount)).send();
        String contractAddress = senderContract.getContractAddress();
        LOG.info("Smart contract deployed to address " + contractAddress);
        LOG.info("View contract at https://rinkeby.etherscan.io/address/" + contractAddress);
        SignedTransactions receiverContract =  SignedTransactions.load(senderContract.getContractAddress(), web3j, receiverCredentials, contractGasProvider);
        Integer nonce = web3j.ethGetTransactionCount(senderContract.getContractAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount().bitCount();
        byte[] data = packArguments(receiverCredentials.getAddress(), contractAddress, nonce, amount);
        Sign.SignatureData signMessage = Sign.signMessage(data, senderCredentials.getEcKeyPair(), false);
        receiverContract.claimPayment(BigInteger.valueOf(amount), BigInteger.valueOf(nonce),  BigInteger.valueOf(signMessage.getV()), signMessage.getR(), signMessage.getS()).send();
        senderContract.kill();
        LOG.info("Killed contract");
        printBalance(web3j, senderCredentials, receiverCredentials);
    }



    private static void printBalance(Web3j web3j,Credentials senderCredentials, Credentials receiverCredentials )
            throws ExecutionException, InterruptedException {
        BigInteger senderBalance = web3j.ethGetBalance(senderCredentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get().getBalance();
        BigInteger receiverBalance = web3j.ethGetBalance(receiverCredentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get().getBalance();
        LOG.info("Sender balance (" + Convert.fromWei(senderBalance.toString(), Convert.Unit.ETHER).toPlainString() + " Ether)");
        LOG.info("Receiver balance (" + Convert.fromWei(receiverBalance.toString(), Convert.Unit.ETHER).toPlainString() + " Ether)");
    }


    /* We need to encode following parameters
        contractAddress + nonce - to prevent replay attack
        recipientAddress - to prevent multi-contract attack
        it is important to use the same order as abi.encodePacked method
        In the Solidity contract
    * */
   private static byte[] packArguments(String recipientAddress, String contractAddress, Integer nonce, Integer amount ){
       String msgHex = recipientAddress
               + Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf(amount), Uint256.MAX_BYTE_LENGTH * 2)
               + Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf(nonce), Uint256.MAX_BYTE_LENGTH * 2)
               + contractAddress.substring(2);
       String hasMsg = Hash.sha3(msgHex);
       return Numeric.hexStringToByteArray(hasMsg);
   }


}
