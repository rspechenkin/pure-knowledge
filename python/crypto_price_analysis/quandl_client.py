import quandl
import pickle
import os

def get_quandl_data(quandl_id, start_date, end_date):
    #Download and cache Quandl dataseries
    cache_path = 'cache'
    file_name = "{}_{}_{}".format(quandl_id.replace('/','-'), start_date, end_date)
    full_path = os.path.join(cache_path, file_name)
    try:
        f = open(full_path, 'rb')
        df = pickle.load(f)   
        print('Loaded {} from cache'.format(quandl_id))
    except (OSError, IOError, FileNotFoundError) as e:
        if not os.path.exists(cache_path):
            os.mkdir(cache_path)
        print('Downloading {} from Quandl'.format(quandl_id))
        df = quandl.get('BCHARTS/KRAKENUSD', returns="pandas", start_date=start_date, end_date=end_date)
        df.to_pickle(full_path)
        print('Cached {} at {}'.format(quandl_id, file_name))
    return df