package model;

import android.graphics.Color;
import android.graphics.Paint;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Stocks {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }

    public void displayChart(String companySymbol, CandleStickChart chart) {
        JSONObject json = null;
        try {
            json = Stocks.readJsonFromUrl("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + companySymbol + "&interval=5min&outputsize=full&apikey=34V7BYNBX3LSQ79T");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        if (json != null) {
            try {
                JSONObject x = json.getJSONObject("Time Series (5min)");
                JSONArray keys = x.names ();

                ArrayList<CandleEntry> ceList = new ArrayList<>();
                ArrayList<Price> n = new ArrayList<>();
                for (int i = keys.length() - 1; i > 0; i--) {
                    String key = keys.getString(i);
                    JSONObject y = x.getJSONObject(key);
                    double open = y.getDouble("1. open");
                    double high = y.getDouble("2. high");
                    double low = y.getDouble("3. low");
                    double close = y.getDouble("4. close");
                    int volume = y.getInt("5. volume");

                    Price price = new Price(key, open, high, low, close, volume);
                    n.add(price);
                }

                Company company = new Company(companySymbol, n);
                ArrayList<String> c = new ArrayList<>();
                for (int i = 0; i < company.getCompanyStockPrices().size(); i++) {
                    c.add(company.getCompanyStockPrices().get(i).getPriceDate());

                    ceList.add(new CandleEntry(i, (float) company.getCompanyStockPrices().get(i).getPriceHigh(), (float) company.getCompanyStockPrices().get(i).getPriceLow(), (float) company.getCompanyStockPrices().get(i).getPriceOpen(), (float) company.getCompanyStockPrices().get(i).getPriceClose()));
                }
                chart.getXAxis().setGranularityEnabled(true);
                chart.getXAxis().setGranularity(1.0f);
                chart.getXAxis().setLabelCount(3);
                chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(c));

                CandleDataSet cds = new CandleDataSet(ceList, "Entries");
                cds.setColor(Color.rgb(80, 80, 80));
                cds.setShadowColor(Color.DKGRAY);
                cds.setShadowWidth(0.7f);
                cds.setDecreasingColor(Color.RED);
                cds.setDecreasingPaintStyle(Paint.Style.FILL);
                cds.setIncreasingColor(Color.rgb(122, 242, 84));
                cds.setIncreasingPaintStyle(Paint.Style.FILL);
                cds.setNeutralColor(Color.BLUE);
                cds.setValueTextColor(Color.RED);
                cds.setDrawValues(false);
                CandleData cd = new CandleData(cds);
                chart.setData(cd);
                chart.invalidate();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
