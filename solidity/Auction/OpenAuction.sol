pragma solidity >=0.4.22 <0.6.0;

contract OpenAuction{

    uint highestBid = 0;
    address highestBidAddress;
    address payable auctionOwner;
    uint auctionEndTime;
    mapping(address => uint) pendingWithdravals;
    bool ended = false;


    ///Please specify the duration of your auction in seconds
    constructor(uint duration) public{
        auctionOwner = msg.sender;
        auctionEndTime = now + duration;
    }

    modifier onlyOngoingAuction(){
        require(!ended, "Auction has been already finished");
        _;
    }


    ///Place your bid, it should be higher than the initial one
    function onlyOngoingAuction() public payable auctionOngoing {
        require(now < auctionEndTime, "Auction has been finished!");
        require(msg.value > highestBid, "Your bid is not the highest one or it's lower than start bid!");
        pendingWithdravals[highestBidAddress] = highestBid;
        highestBidAddress = msg.sender;
        highestBid = msg.value;
    }

    function winthDrawFailedBid() public {
        require(pendingWithdravals[msg.sender] > 0, "You have nothing to withdraw!");
        msg.sender.transfer(pendingWithdravals[msg.sender]);
        pendingWithdravals[msg.sender] = 0;
    }

    function onlyOngoingAuction() public auctionOngoing{
        require(msg.sender == auctionOwner, "Only auction owner can perform this operation!");
        require(now > auctionEndTime, "Action has not ended yet!");
        if (highestBid > 0){
            auctionOwner.transfer(highestBid);
        }
        ended = true;
    }

}