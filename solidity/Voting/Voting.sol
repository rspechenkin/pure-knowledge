pragma solidity >=0.4.22 <0.6.0;

//A simple voting contract with following rules:
// 1. Voting started when deploying the contract providing addresses of the voters and proposals
// 2. One person can't vote twice
// 3. Only specific accounts can vote (the ones that passed during the contract creation)
// 4. To conclude voting the owner of the contract should invoke "concludeVoting" function
// 5. Voting can't be concluded if there are 2 or more proposals with the same maximum number of votes
// 6. Voting can't be concluded if nobody voted
// 7. No more voting is possible after concluding


contract Voting {

    address private owner;
    bool votingAllowedFlag;

    constructor (bytes32[] memory proposalNames, address[] memory voterAddresses) public{
        owner = msg.sender;
        for (uint i = 0; i < proposalNames.length; i++) {
            proposals.push(Proposal(proposalNames[i], 0));
        }
        for (uint i = 0; i < voterAddresses.length; i++) {
            voters[voterAddresses[i]] = true;
        }
        votingAllowedFlag = true;
    }

    struct Proposal{
        bytes32 name;
        uint votes;
    }

    Proposal[] proposals;
    mapping(address => bool) voters;

    modifier votingAllowed(){
        require(votingAllowedFlag == true, "Voting has not yet been started or has been already finished!");
        _;
    }

    modifier onlyOwner(){
        require(msg.sender == owner, "Only contract owner can perform this action!");
        _;
    }

    modifier canVote(){
        require(voters[msg.sender] == true, "You have no right to vote or you have already voted for this proposal!");
        _;
    }

    function vote(uint proposalIndex) public votingAllowed canVote{
        proposals[proposalIndex].votes += 1;
        voters[msg.sender] = false;
    }

    function destroyContract() public onlyOwner{
        selfdestruct(msg.sender);
    }


    function concludeVoting() onlyOwner votingAllowed public returns(bytes32 winner) {
        uint maxVotes = 0;
        uint numberOfMaxValues = 0;
        for (uint i=0; i<proposals.length; i++){
            if (proposals[i].votes == maxVotes){
                numberOfMaxValues++;
            }
            if (proposals[i].votes > maxVotes){
                maxVotes = proposals[i].votes;
                winner = proposals[i].name;
                numberOfMaxValues = 1;
            }
        }
        require(maxVotes > 0, "Nobody voted!");
        require(numberOfMaxValues == 1, "More than 1 proposal has the same number of votes!");
        votingAllowedFlag = false;
        return winner;
    }
}
