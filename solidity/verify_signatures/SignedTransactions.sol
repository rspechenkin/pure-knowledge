pragma solidity >=0.4.24 <0.6.0;

contract SignedTransactions {

    //sh shell/sol_java_wrapper.sh -c solidity/payment_channel/SignedTransactions.sol -p com.test -o java/baseModule/src/main/java/

    address payable owner;
    mapping(uint256 => bool) usedNonces;

    modifier onlyOwner(){
        require(owner == msg.sender, "You're not allowed to perform this operation!");
        _;
    }

    constructor() public payable{
        owner = msg.sender;
    }

    function claimPayment(uint256 amount, uint256 nonce, uint8 v, bytes32 r, bytes32 s) public {
        require(!usedNonces[nonce], 'Payment has been already processed');
        usedNonces[nonce] = true;
        bytes32 sha3Sum = keccak256(abi.encodePacked(msg.sender, amount, nonce, this));
        require (ecrecover(sha3Sum, v, r, s) == owner, 'Your signature is not valid!');
        msg.sender.transfer(amount);
    }

    function kill() public onlyOwner{
        selfdestruct(owner);
    }
}
