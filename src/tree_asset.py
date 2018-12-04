#!/usr/bin/python3
# python 3.7

from anytree.exporter import DotExporter
from anytree import Node, PreOrderIter, LevelOrderIter
import api
import config
import json
import main
import strategies
import utils
import ratio
import queue
import wallet

# construction naive avec toute la solution
def get_naive_tree(assets: list, parent=None) -> Node:
    if len(assets) == 1:
        return Node(utils.get_asset_id(assets[0]),
                    parent=parent,
                    asset=assets[0])

    for a in assets:
        n = Node(str(utils.get_asset_id(a)), parent=parent, asset=a, sharpe=-1)

        if n.depth is not 1:
            ids = get_list_ids(n)
            wallet_rend = wallet.compute_rend_without_weight(ids)

            if float(strategies.WALLET_MIN_REND) < float(wallet_rend):
                new_assets = remove_list(assets, a)
                get_naive_tree(new_assets, n)
            else:
                n.parent = None
                n = None
                return
        else:
            new_assets = remove_list(assets, a)
            get_naive_tree(new_assets, n)


    return parent


def sharp(node: Node):
    a_id = utils.get_asset_id(node.asset)
    config.wallet.add_asset(a_id, 1)
    sharp = config.wallet.get_sharp()
    config.wallet.rm_asset(a_id)
    
    node.sharp = sharp

    return sharp > 0.3


def tree_walk(root: Node):
    q = queue.Queue()
    ids = list()

    q.put(root)
    while q.not_empty:
        n = q.get()

        if len(n.children) == 0:
            break

        id_child = list()
        for child in n.children:
            id_child.append(utils.get_asset_id(child.asset))
            q.put(child)

        if n.asset == None:
            continue

    return

    for elm in q:
        if elm.sharp > max_sharp:
            max_node = elm
            max_sharp = elm.sharp

    for anc in max_node.ancestors:
        ids.append((utils.get_asset_id(anc.asset), 1))
    
    return ids


def tree_walk_sharp(root: Node):
    max_sharp = -1
    max_node = None
    q = queue.Queue()
    ids = list()

    q.put(root)

    while q.not_empty:
        n = q.get()

        if len(n.children) == 0:
            break

        for child in n.children:
            if sharp(child):
                q.put(child)

    for elm in q:
        if elm.sharp > max_sharp:
            max_node = elm
            max_sharp = elm.sharp

    for anc in max_node.ancestors:
        ids.append((utils.get_asset_id(anc.asset), 1))
    
    return ids


def remove_list(l: list, elm):
    r = list(l)
    r.remove(elm)

    return r


def get_list_ids(node: Node) -> list():
    ret = list()
    ret.append(utils.get_asset_id(node.asset))

    for anc in node.ancestors:
        if anc.name.find("stock") == -1 \
                and anc.name.find("fund") == -1:
            ret.append(utils.get_asset_id(anc.asset))

    return ret


def get_list_ids_weight(node: Node) -> list():
    ret = list()
    ret.append(utils.get_asset_id(node.asset))

    for anc in node.ancestors:
        if anc.name.find("root") == -1:
            ret.append((utils.get_asset_id(anc.asset), anc.weight))

    return ret


def nodenamefunc(node):
    return '%s:%s' % (node.name, node.depth)


def pre_order(roots: list()):
    for r in roots:
        max_depth = -1

        for node in PreOrderIter(r):
            if node.depth > max_depth:
                max_depth = node.depth

        print('depth max for ' + str(r.name) + ': ' + str(max_depth))


def export_tree(root: Node, output="root.dot"):
    main.print_notif("Exportation de l'arbre")
    dot = DotExporter(root, nodenamefunc=nodenamefunc)
    dot.to_dotfile(output)
