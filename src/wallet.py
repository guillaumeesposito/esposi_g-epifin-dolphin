#!/usr/bin/python3

from anytree import Node
import utils
import time
import requests
import json
import main
import config
import api
import ratio


def get_total_amount_assets(list_assets, currency) -> float:
    amount = 0
    for (a_id, q) in list_assets:
        curr = 1
        if utils.get_asset_currency_by_id(a_id) != currency:
            curr = utils.rate_tgt_curr(a_id, currency)
        amount += utils.get_last_price_by_id(a_id) * q / curr
    return amount


# calcul du rendement sans le poids
# list_id = liste des id asset
def compute_rend_without_weight(list_id: list) -> float:
    r = 0
    w = len(list_id)
    for a_id in list_id:
        r += ratio.get_rendement_by_id(a_id) / w

    return r

# calcul du rendement avec le poids 
# list_id = liste de pair de valeur (id_asset, quantity)
def compute_rend_with_weight(list_assets: list, currency='EUR') -> float:
    r = 0
    for (a_id, q) in list_assets:
        r += ratio.get_rendement_by_id(a_id) * \
                (q * utils.get_last_price_by_id(a_id) / \
                get_total_amount_assets(list_assets, currency))

    return r


class Wallet():
    id = 0                  # contains the id of the portfolio
    label = 'epita_ptf_6'   # portfolio label 
    assets = list()         # contains pair (assets_id, quantity)
    currency = 'EUR'        # currency
    ret = -1                # the return of the wallet
    volatility = -1         # the volatility of the wallet
    cur_tree = None         # contains the actual tree
    best_tree = None        # contains the tree which gives the best ret for the wallet


    def __init__(self, id=0, assets=list(), currency='EUR', ret=-1, volatility=-1):

        main.print_notif("Creation du portefeuille")

        self.id = id        
        self.assets = assets
        self.currency = currency
        self.ret = ret
        self.volatility = volatility
        self.cur_tree = None
        self.best_tree = None

        if id == 0:
            for a in config.assets:
                if utils.get_asset_type(a) == 'PORTFOLIO':
                    self.id = utils.get_asset_id(a)
            if self.id == 0:
                raise RuntimeError("Error ID portfolio")


    def set_ret(self, ret):
        self.ret = ret


    def add_asset(self, asset: int, quantity: int):
        if type(asset) == type(dict()):
            asset = utils.get_asset_id(asset)
        self.assets.append((asset, quantity))


    def rm_asset(self, asset: int):
        for i, (a, q) in enumerate(self.assets, 0):
            if a == asset:
                self.assets.pop(i)


    def calc_ret(self):
        pass


    def calc_volatility(self):
        pass


    def submit_to_server(self):
        list_asset = []
        for (asset, quantity) in self.assets:
            a = {}
            a["asset"] = {
                "asset": asset,
                "quantity": quantity
                }
            list_asset.append(a)
        
        payload = {
            "currency": {"code": self.currency},
            "label": self.label,
            "type": "front",
            "values": {"2012-01-02": list_asset}
        }
        
        main.print_notif("Soumission du portefeuille au serveur...")
        result = requests.put(utils.URL + '/portfolio/' + str(self.id) + 
                    '/dyn_amount_compo',
                    auth=utils.AUTH,
                    data=json.dumps(payload))
        if result.status_code != 200:
            print ("Error submit portfolio: code " + str(result.status_code))
            return 1
        main.print_notif("  Portfolio soumit avec succes !")

        return 0

    def print(self):
        s = "\nPortefeuille " + self.label + ":\n" + 25 * '-' + "\n"
        for (a_id, q) in self.assets:
            s += "\tid:\t\t" + str(a_id) + "\n\tlabel:\t\t" + \
                utils.get_asset_label_by_id(a_id) + \
                "\n\tquantity:\t" + str(q) + "\n\n"
            
        s += '-' * 25 + "\n"
        s += "\nTotal amount: " + str(self.get_total_amount()) + " " +  self.currency
        s += "\nRendement: " + str(self.get_rend() * 100) + " %\n"
        print (s)
    
    def print_portfolio_server(self):
        self.submit_to_server()
        print (json.dumps(api.get_portfolio(self.id), indent=2))


    def get_rend(self) -> float:
        return compute_rend_with_weight(self.assets, self.currency)


    def get_total_amount(self) -> float:
        return get_total_amount_assets(self.assets, self.currency)


    # proportion de la devise dans le portefeuille
    def get_amount_in_currency(self, currency):
        pass        


    def get_sharp(self) -> float:
        self.submit_to_server()
        return float(api.compute_ratio([ratio.sharp], [self.id])\
                [str(self.id)][str(ratio.sharp)]['value'].replace(',', '.'))
