#!/usr/bin/python3

import json

import requests

import config
import api

URL = 'https://hostname:port/api/v1/'
AUTH = ('username', 'password')
START_DATE = "2012-01-02"
END_DATE = "2018-08-31"


def get_data(endpointApi, date=None, full_response=False, columns=list()):
    payload = {'date': date, 'fullResponse': full_response, "columns": columns }
    res = requests.get(URL + endpointApi,
            params=payload,
            auth=AUTH,
            verify=False)
    if res.status_code != 200:
        print ("Error code", res.status_code)
        raise Exception('Error status code from API request')
    return json.loads(res.content.decode('utf-8'))


def get_config(config_path):
    try:
        global URL
        global AUTH
        config_file = open(config_path, 'r')
        lines = config_file.readlines()
        URL = lines[0].rstrip() + '/api/v1'
        AUTH = (lines[1].split(' ')[1].rstrip(), lines[2].split(' ')[1].rstrip())
        config_file.close()
    except IOError:
        print ("Cannot open " + config_path)
        return 1
    return 0


def get_asset_id(asset) -> int:
    if 'ASSET_DATABASE_ID' in asset:
        return int(asset['ASSET_DATABASE_ID']['value'])
    else:
        raise RuntimeError


def get_asset_label(asset):
    return asset['LABEL']['value']


def get_asset_label_by_id(id: int):
    for a in config.assets:
        if get_asset_id(a) == id:
            return get_asset_label(a)


def get_asset_type(asset):
    return asset['TYPE']['value']


def get_asset_last_close_value_in_curr(asset):
    return asset['LAST_CLOSE_VALUE_IN_CURR']['value']


def get_asset_return(asset_quote):
    return asset_quote['return']['value']


def is_stock(asset) -> bool:
    return get_asset_type(asset) == 'STOCK'


def is_fund(asset) -> bool:
    return get_asset_type(asset) == 'FUND'


def is_bond(asset) -> bool:
    return get_asset_type(asset) == 'BOND'  # Obligations

def is_exchange(asset) -> bool:
    return get_asset_type(asset) == 'EXCHANGE'  # Taux de change


def is_rate(asset) -> bool:
    return get_asset_type(asset) == 'RATE'  # Taux


def get_last_price(asset) -> float:
    price = float(asset['LAST_CLOSE_VALUE_IN_CURR']['value'].split(' ')[0].replace(',', '.'))
    return price

def get_last_price_by_id(id: int) -> float:
    for a in config.assets:
        if get_asset_id(a) == id:
            return get_last_price(a)


def get_asset_currency(asset):
    return asset['CURRENCY']['value']


def get_asset_currency_by_id(id: int):
    for a in config.assets:
        if get_asset_id(a) == id:
            return get_asset_currency(a)


def rate_tgt_curr(id_asset: int, target_currency):
    return float(api.convert_currency(get_asset_currency_by_id(id_asset),
    target_currency, date="2018-08-31")['rate']['value'].replace(',', '.'))
