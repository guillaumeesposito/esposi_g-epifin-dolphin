#!/usr/bin/python3

import utils
import json
import requests

### ACTIFS ###
def get_assets(date=None, fullResponse=False, columns=None) -> dict:
    url = utils.URL + "/asset"
    parameters = {'date': date,
                  'fullResponse': fullResponse,
                  'columns': columns}

    result = requests.get(url, params=parameters, auth=utils.AUTH, verify=False)
    #print (result.url)

    if result.status_code == 200:
        return json.loads(result.content.decode('utf-8'))
    elif result.status_code == 401:
        raise Exception("401 Unauthorized")
    elif result.status_code == 500:
        raise Exception("500 Internal Server Error")
    else:
        raise Exception("Unknown Error")

def get_asset_by_id(id: int, date=None, fullResponse=False, columns=None) -> dict:
    url = utils.URL + "/asset/" + id
    parameters = {'date': date,
                  'fullResponse': fullResponse,
                  'columns': columns}

    result = requests.get(url, params=parameters, auth=utils.AUTH, verify=False)
    
    if result.status_code == 200:
        return json.loads(result.content.decode('utf-8'))
    elif result.status_code == 401:
        raise Exception("401 Unauthorized")
    elif result.status_code == 403:
        raise Exception("403 Forbidden")
    elif result.status_code == 404:
        raise Exception("404 Not Found")
    elif result.status_code == 500:
        raise Exception("500 Internal Server Error")
    else:
        raise Exception("Unknown Error")

def get_asset_attribute(id: int, attributeName: str, date=None, fullResponse=False) -> dict:
    url = utils.URL + "/assets/" + id + "/attribute/" + attributeName
    parameters = {'date': date,
                  'fullResponse': fullResponse}

    result = requests.get(url, params=parameters, auth=utils.AUTH, verify=False)
    
    if result.status_code == 200:
        return json.loads(result.content.decode('utf-8'))
    elif result.status_code == 401:
        raise Exception("401 Unauthorized")
    elif result.status_code == 403:
        raise Exception("403 Forbidden")
    elif result.status_code == 404:
        raise Exception("404 Not Found")
    elif result.status_code == 500:
        raise Exception("500 Internal Server Error")
    else:
        raise Exception("Unknown Error")

### COTATION ###
def get_asset_quote(id: int, startDate=None, endDate=None) -> dict:
    url = utils.URL + "/asset/" + id + "/quote"
    parameters = {'start_date': startDate,
                  'end_date': endDate}

    result = requests.get(url,
                          params=parameters,
                          auth=utils.AUTH,
                          verify=False)

    if result.status_code == 200:
        return json.loads(result.content.decode('utf-8'))
    elif result.status_code == 401:
        raise Exception("401 Unauthorized")
    elif result.status_code == 403:
        raise Exception("403 Forbidden")
    elif result.status_code == 404:
        raise Exception("404 Not Found")
    elif result.status_code == 500:
        raise Exception("500 Internal Server Error")
    else:
        raise Exception("Unknown Error")

### COMPOSITION HISTORIQUE ###
def get_portfolio(id: int) -> dict:
    url = utils.URL + "/portfolio/" + str(id) + "/dyn_amount_compo"

    result = requests.get(url, auth=utils.AUTH, verify=False)

    if result.status_code == 200:
        return json.loads(result.content.decode('utf-8'))
    elif result.status_code == 401:
        raise Exception("401 Unauthorized")
    elif result.status_code == 403:
        raise Exception("403 Forbidden")
    elif result.status_code == 404:
        raise Exception("404 Not Found")
    elif result.status_code == 500:
        raise Exception("500 Internal Server Error")
    else:
        raise Exception("Unknown Error")

def update_portfolio(id: int, portfolio: dict):
    url = utils.URL + "/portfolio/" + id + "/dyn_amount_compo"
    head = {'Content-Type':'application/json'}
    body = json.dumps(portfolio)

    result = requests.put(url,
                          header=head,
                          data=body,
                          auth=utils.AUTH,
                          verify=False)

    if result.status_code == 200:
        print("200 Success")
        return
    elif result.status_code == 401:
        raise Exception("401 Unauthorized")
    elif result.status_code == 403:
        raise Exception("403 Forbidden")
    elif result.status_code == 404:
        raise Exception("404 Not Found")
    elif result.status_code == 500:
        raise Exception("500 Internal Server Error")
    else:
        raise Exception("Unknown Error")

### RATIOS ###
def get_ratios_list() -> dict:
    url = utils.URL + "/ratio"
    
    result = requests.get(url,
                          auth=utils.AUTH,
                          verify=False)

    if result.status_code == 200:
        return json.loads(result.content.decode('utf-8'))
    elif result.status_code == 401:
        raise Exception("401 Unauthorized")
    elif result.status_code == 500:
        raise Exception("500 Internal Server Error")
    else:
        raise Exception("Unknown Error")

def compute_ratio(id_ratio: list, id_asset: list,
        id_bench=None, 
        start_date=utils.START_DATE,
        end_date=utils.END_DATE,
        fullResponse=False) -> dict:
    
    if type(id_ratio) != type(list()) or type(id_asset) != type(list()):
        raise Exception("id_ratio and id_asset must be list()")

    url = utils.URL + "/ratio/invoke"
    parameters = {'fullResponse': fullResponse}
    body = {
        "ratio": id_ratio,
        "asset": id_asset,
        "bench": id_bench,
        "start_date": start_date,
        "end_date": end_date,
        "frequency": None
    }

    result = requests.post(url,
                           data=json.dumps(body),
                           params=parameters,
                           auth=utils.AUTH,
                           verify=False)

    if result.status_code == 200:
        return json.loads(result.content.decode('utf-8'))
    elif result.status_code == 401:
        raise Exception("401 Unauthorized")
    elif result.status_code == 500:
        raise Exception("500 Internal Server Error")
    else:
        raise Exception("Unknown Error")


def convert_currency(curr_src, curr_dest, date=None, fullResponse=False):
    url = utils.URL + "/currency/rate/" + curr_src + "/to/" + curr_dest
    params = {
        "date": date,
        "fullResponse": fullResponse
    }

    result = requests.get(url,
                           params=params,
                           auth=utils.AUTH,
                           verify=False)

    if result.status_code == 200:
        return json.loads(result.content.decode('utf-8'))
    elif result.status_code == 401:
        raise Exception("401 Unauthorized")
    elif result.status_code == 500:
        raise Exception("500 Internal Server Error")
    else:
        raise Exception("Unknown Error")
