#!/bin/bash
#设置环境变量
. ~/.bashrc
. ~/.bash_profile
export PM2_HOME=/alidata1/app/ishare-admin-ui
mkdir -p $PM2_HOME
pm2 delete  all
yarn install
pm2 --no-daemon start pm2.config.json --name ishare-admin-ui