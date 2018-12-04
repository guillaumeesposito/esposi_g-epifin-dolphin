#!/usr/bin/python3

import urllib3
import certifi

verbose = None
assets = None
tree_output = None
ratios = None
wallet = None
http = None
http = urllib3.PoolManager(
        cert_reqs='CERT_REQUIRED',
        ca_certs=certifi.where())