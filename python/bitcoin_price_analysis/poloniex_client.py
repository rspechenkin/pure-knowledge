from datetime import datetime
import pickle
import pandas as pd
import os

base_url = 'https://poloniex.com/public?command=returnChartData&currencyPair={}&start={}&end={}&period={}'
pediod = 86400 # 86,400 seconds per day


def get_poloniex_data(poloniex_pair, start_date, end_date):
    url = base_url.format(poloniex_pair, start_date.timestamp(), end_date.timestamp(), pediod)
    file_name = "{}_{}_{}".format(poloniex_pair, start_date, end_date)
    cache_path = 'cache'
    full_path = os.path.join(cache_path, file_name)
    try:
        f = open(full_path, 'rb')
        data_df = pickle.load(f)
        print('Loaded {} from cache'.format(url))
    except (OSError, IOError, FileNotFoundError) as e:
        if not os.path.exists(cache_path):
            os.mkdir(cache_path)
        print('Downloading {}'.format(url))
        data_df = pd.read_json(url)
        data_df.to_pickle(full_path)
        print('Cached {} at {}'.format(url, file_name))
    data_df = data_df.set_index('date')
    return data_df


def get_altcoin_data(altcoins, start_date, end_date):
    altcoin_data = {}
    start_date = datetime.strptime(start_date, '%Y-%m-%d') # get data from the start of 2015
    end_date = datetime.strptime(end_date, '%Y-%m-%d')     # up until today
    for altcoin in altcoins:
        coinpair = 'BTC_{}'.format(altcoin)
        crypto_price_df = get_poloniex_data(coinpair, start_date, end_date)
        altcoin_data[altcoin] = crypto_price_df
    return altcoin_data

