import numpy as np
import pandas as pd
import poloniex_client
import quandl_client
import plotly.offline as py
import plotly.graph_objs as graph_objs

altcoin_names = ['ETH','LTC','XRP','ETC','DASH','SC','XMR','XEM']
quandl_dataset_name = 'BCHARTS/KRAKENUSD'                           #more info at https://www.quandl.com
start_date="2016-01-01"
end_date="2017-01-01"


def draw_correlation_heatmap(df, title):
    heatmap = graph_objs.Heatmap(
        z=df.corr(method='pearson').as_matrix(),
        x=df.columns,
        y=df.columns,
        colorbar=dict(title='Pearson Coefficient'),
    )
    layout = graph_objs.Layout(title=title)
    heatmap['zmax'] = 1.0
    heatmap['zmin'] = -1.0
    fig = graph_objs.Figure(data=[heatmap], layout=layout)
    py.plot(fig)


if __name__ == '__main__':
    altcoins_df = poloniex_client.get_altcoin_data(altcoin_names, start_date, end_date)                  #get data altcoins price from Poloniex
    bitcoin_df = quandl_client.get_quandl_data(quandl_dataset_name, start_date, end_date)                #get Bitcoin price from KRAKEN
    bitcoin_df.replace(0, np.nan, inplace=True)                                                          #clean incorrect data (Bitcoin price was never 0)
    result = {}
    for altcoin in altcoin_names:                                                                        #calculate altcoins price in USD
        altcoin_price_chart = altcoins_df[altcoin]['weightedAverage'] * bitcoin_df['Weighted Price']
        result[altcoin] = altcoin_price_chart
    result["BTC"] = bitcoin_df['Weighted Price']
    result_df = pd.DataFrame(result)
    corr = result_df.pct_change().corr(method='pearson')                   #calculate Pearson Coefficient
    draw_correlation_heatmap(corr, 'Correlation analysis based on the Pearson Coefficient')
