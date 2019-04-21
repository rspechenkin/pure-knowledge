const RomansTokenContract = artifacts.require("RomansToken");

module.exports = function(deployer) {
  deployer.deploy(RomansTokenContract, 1000000);
};
