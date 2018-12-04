#!/usr/bin/python3

import api
import config
import json
import utils

sharp = 20
beta = 15
correlation = 19
exposition_action = 29
RDT = 16
rendement = 21
rendement_a = 17
var_histo = 22
volatilite = 18


def get_all_ratios() -> list:
    if config.ratios == None:
        all_asset_id = list()
        for a in config.assets:
            all_asset_id.append(utils.get_asset_id(a))
        
        config.ratios = api.compute_ratio(
                [
                    sharp,
                    rendement,
                    RDT,
                    rendement_a,
                    var_histo,
                    volatilite
                ],
                all_asset_id)

    return config.ratios


def get_ratios_by_id(id: int):
    return get_all_ratios()[str(id)]


def get_sharp_by_id(id: int) -> float:
    r = get_ratios_by_id(id)[str(sharp)]
    if r['type'] == 'error':
        raise Exception("Unavailable ratio")

    return float(r['value'].replace(",", "."))


def get_volatilite_by_id(id: int) -> float:
    r = get_ratios_by_id(id)[str(volatilite)]
    if r['type'] == 'error':
        raise Exception("Unavailable ratio")

    return float(r['value'].replace(",", "."))


def get_rendement_by_id(id: int) -> float:
    r = get_ratios_by_id(id)[str(rendement)]
    if r['type'] == 'error':
        raise Exception("Unavailable ratio")

    return float(r['value'].replace(",", "."))


def get_RDT_by_id(id: int) -> float:
    r = get_ratios_by_id(id)[str(RDT)]
    if r['type'] == 'error':
        raise Exception("Unavailable ratio")

    return float(r['value'].replace(",", "."))


def get_var_histo_by_id(id: int) -> float:
    r = get_ratios_by_id(id)[str(var_histo)]
    if r['type'] == 'error':
        raise Exception("Unavailable ratio")

    return float(r['value'].replace(",", "."))


def get_rendement_a_by_id(id: int) -> float:
    r = get_ratios_by_id(id)[str(rendement_a)]
    if r['type'] == 'error':
        raise Exception("Unavailable ratio")

    return float(r['value'].replace(",", "."))


def get_exposition_by_id(id: int) -> float:
    r = get_ratios_by_id(id)[str(exposition_action)]
    if r['type'] == 'error':
        raise Exception("Unavailable ratio")

    return float(r['value'].replace(",", "."))
