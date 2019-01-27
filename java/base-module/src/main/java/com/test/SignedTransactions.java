package com.test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.0.1.
 */
public class SignedTransactions extends Contract {
    private static final String BINARY = "608060405260008054600160a060020a031916331790556103d1806100256000396000f3fe608060405260043610610045577c0100000000000000000000000000000000000000000000000000000000600035046301857261811461004a57806341c0e1b514610091575b600080fd5b34801561005657600080fd5b5061008f600480360360a081101561006d57600080fd5b5080359060208101359060ff60408201351690606081013590608001356100a6565b005b34801561009d57600080fd5b5061008f6102de565b60008481526001602052604090205460ff161561014a57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152602260248201527f5061796d656e7420686173206265656e20616c72656164792070726f6365737360448201527f6564000000000000000000000000000000000000000000000000000000000000606482015290519081900360840190fd5b6000848152600160208181526040808420805460ff19168417905580516c0100000000000000000000000033810282850152603482018b9052605482018a905230026074820152815160688183030181526088820180845281519185019190912086549690915260a8820180845281905260ff891660c883015260e8820188905261010882018790529151919473ffffffffffffffffffffffffffffffffffffffff169392610128808301939192601f198301929081900390910190855afa15801561021a573d6000803e3d6000fd5b5050506020604051035173ffffffffffffffffffffffffffffffffffffffff161415156102a857604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601c60248201527f596f7572207369676e6174757265206973206e6f742076616c69642100000000604482015290519081900360640190fd5b604051339087156108fc029088906000818181858888f193505050501580156102d5573d6000803e3d6000fd5b50505050505050565b60005473ffffffffffffffffffffffffffffffffffffffff16331461038a57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152602d60248201527f596f75277265206e6f7420616c6c6f77656420746f20706572666f726d20746860448201527f6973206f7065726174696f6e2100000000000000000000000000000000000000606482015290519081900360840190fd5b60005473ffffffffffffffffffffffffffffffffffffffff16fffea165627a7a72305820c775386d58a7511e518f50d2d9dd2504d9ac22bdc05862f2b83a23395c7ce1700029";

    public static final String FUNC_CLAIMPAYMENT = "claimPayment";

    public static final String FUNC_KILL = "kill";

    @Deprecated
    protected SignedTransactions(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SignedTransactions(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SignedTransactions(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SignedTransactions(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> claimPayment(BigInteger amount, BigInteger nonce, BigInteger v, byte[] r, byte[] s) {
        final Function function = new Function(
                FUNC_CLAIMPAYMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.generated.Uint256(nonce), 
                new org.web3j.abi.datatypes.generated.Uint8(v), 
                new org.web3j.abi.datatypes.generated.Bytes32(r), 
                new org.web3j.abi.datatypes.generated.Bytes32(s)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> kill() {
        final Function function = new Function(
                FUNC_KILL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static SignedTransactions load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SignedTransactions(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SignedTransactions load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SignedTransactions(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SignedTransactions load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SignedTransactions(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SignedTransactions load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SignedTransactions(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SignedTransactions> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger initialWeiValue) {
        return deployRemoteCall(SignedTransactions.class, web3j, credentials, contractGasProvider, BINARY, "", initialWeiValue);
    }

    public static RemoteCall<SignedTransactions> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger initialWeiValue) {
        return deployRemoteCall(SignedTransactions.class, web3j, transactionManager, contractGasProvider, BINARY, "", initialWeiValue);
    }

    @Deprecated
    public static RemoteCall<SignedTransactions> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployRemoteCall(SignedTransactions.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    @Deprecated
    public static RemoteCall<SignedTransactions> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployRemoteCall(SignedTransactions.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }
}
