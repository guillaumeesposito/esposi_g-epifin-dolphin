#!/usr/bin/python3

import json
import time

from anytree import Node

import api
import config
import main
import ratio
import tree_asset
import utils
import wallet

WALLET_MIN_REND = 2.8
WALLET_MAX_REND = 3


def naive(portfolio: wallet.Wallet, print_portfolio: bool, export_tree: bool):
    ### FILTER ASSETS
    stock_assets = list()
    fund_assets = list()

    for a in config.assets:
        a_id = utils.get_asset_id(a)

        if utils.is_stock(a) is True \
                and ratio.get_rendement_by_id(a_id) > 0.0 \
                and 0.0 < ratio.get_volatilite_by_id(a_id) < 0.25:
            stock_assets.append(a)

        if utils.is_fund(a) is True \
                and ratio.get_rendement_a_by_id(a_id) > 0.0:
            fund_assets.append(a)

    print("len(stock_asset) = " + str(len(stock_assets)))

    ### TREE CREATION ###
    # main.print_notif("Creation de l'arborescence")
    # t0 = time.clock()

    # stock_roots = list()

    # for i in range(0, len(stock_assets), 10):
    #     root = Node('stock' + str(i), asset=None, weight=-1, sharpe=-1)
    #     root = tree_asset.get_naive_tree(stock_assets[i:i+10], root)
    #     stock_roots.append(root)

    # main.print_notif("  creation terminée en " + str(time.clock() - t0) + "s")

    ### PARCOURS DE L'ARBRE
    # main.print_notif("Parcours des solutions")
    # t0 = time.clock()

    # ids = list()
    # for r in stock_roots:
    #     ids = tree_asset.tree_walk(r)

    # main.print_notif("  parcours terminé en " + str(time.clock() - t0) + "s")
    
    # ## EXPORT DOT FILE FOR TREE DEBUG ###
    # if export_tree:
    #     for r in stock_roots:
    #         tree_asset.export_tree(r, r.name + ".dot")

    ### FILL PORTFOLIO ###
    try:
        # NAV ENTRE 1% & 10%
        contrainte_nav = list()
        for a in stock_assets:
            a_id = utils.get_asset_id(a)
            if ratio.get_rendement_by_id(a_id) < 0.1:
                contrainte_nav.append(a_id)

        max_sharp = -1
        id_best_sharp = -1
        for a_id in contrainte_nav:
            sharp = ratio.get_sharp_by_id(a_id)
            if max_sharp < sharp:
                max_sharp = sharp
                id_best_sharp = a_id
        
        portfolio.add_asset(id_best_sharp, 10)

        best_sharp = list()
        for a in config.assets:
            a_id = utils.get_asset_id(a)
            if utils.is_stock(a) and ratio.get_sharp_by_id(a_id) > 1.2:
                best_sharp.append(a)

        for a in best_sharp[0:17]:
            portfolio.add_asset(a, 50)


        best_sharp_fund = list()
        for a in config.assets:
            a_id = utils.get_asset_id(a)
            if utils.is_fund(a) and ratio.get_sharp_by_id(a_id) > 1.2:
                best_sharp_fund.append(a)
        
        i = 20 - len(best_sharp) - 1
        for f in best_sharp_fund:
            if i <= 0:
                break
            portfolio.add_asset(f, 10)
            i = i - 1

        # portfolio.get_sharp()

        if print_portfolio:
            portfolio.print()

    except Exception as e:
        print(e)
