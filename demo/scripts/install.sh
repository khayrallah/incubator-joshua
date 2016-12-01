#! /bin/bash

#usage: ./install.sh or bash install.sh 
#DO NOT RUN sh install.sh on ubuntu, this needs to run in bash not dash 

#install dependencies
echo "install dependencies"
sudo apt-get --yes --force-yes update
sudo apt-get --yes --force-yes install default-jdk
sudo apt-get --yes --force-yes install maven

#clone the joshua repo
echo "clone the Joshua repo"
git clone https://github.com/apache/incubator-joshua.git

#compile joshua
echo "compile Joshua"
cd incubator-joshua/
export JOSHUA=$(pwd)
echo "export JOSHUA=$JOSHUA" >> ~/.bashrc
source ~/.bashrc
mvn package
