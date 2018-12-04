#!/usr/bin/python3

from optparse import OptionParser
import json
import sys
import time

from anytree.exporter import DotExporter
from anytree import Node

import api
import config
import parcours
import strategies
import tree_asset
import utils
import wallet


def print_notif(msg):
    if config.verbose:
        print("\t> " + msg)


def get_assets(filename):
    try:
        print_notif("Recuperation des assets dans le fichier " + filename)
        f = open(filename, 'r')
        config.assets = json.load(f)
        f.close()
    except IOError: # file not found
        print_notif("Recuperation des assets sur le serveur")
        config.assets = api.get_assets()
        with open(filename, 'w') as outfile:
            json.dump(config.assets, outfile)


def main():
    ### PARSING COMMAND LINE ###
    parser = OptionParser()
    parser.add_option("-c", "--config", action="store", type="string",
            dest="config", metavar="FILE", help="input configuration file",
            default="login")
    parser.add_option("-a", "--assets", action="store", type="string",
            dest="assets", metavar="FILE", help="input assets file formated in json. Avoid to call the server to fetch assets. If file not found, assets will be fetch on server and written on file with specified name", default="assets.json")
    parser.add_option("-q", "--quiet", action="store_false", dest="verbose",
            default=True)
    parser.add_option("-s", "--submit", action="store_true", dest="submit",
            help="Submit wallet to server", default=False)
    parser.add_option("-o", "--output", action = "store", type = "string",
        dest="tree_output", metavar="FILE", help="write output to FILE .dot format", default="root.dot")

    (options, args) = parser.parse_args()

    if len(sys.argv) == 0: # number of required option
        parser.error("incorrect number of arguments")

    ### GET CONFIG FILE ###
    if utils.get_config(options.config):
        return 1

    config.tree_output = options.tree_output
    config.verbose = options.verbose

    ### GET ALL ASSETS ###
    get_assets(options.assets)

    ### PORTFOLIO ###
    config.wallet = wallet.Wallet()

    ### APPLY STRATEGY
    strategies.naive(config.wallet, True, True)

    if options.submit:
        config.wallet.submit_to_server()

    return 0

if __name__ == "__main__":
    main()
