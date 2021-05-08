package model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Stocks {
    JSONObject jsonMetaData;
    JSONObject jsonPrices;

    public void setCompanyData(Context context, RequestQueue mQueue, Company company, CandleStickChart chart,
                               TextView currentPrice, TextView priceChange, TextView description, TextView sector, TextView peRatio,
                               TextView pegRatio, TextView lastDividend, TextView employees, TextView analystTargetPrice,
                               TextView pbRatio, TextView payoutRatio) {
        String url1 = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + company.getCompanySymbol() + "&interval=5min&outputsize=full&apikey=34V7BYNBX3LSQ79T";
        String url2 = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" + company.getCompanySymbol() + "&apikey=NFK6HMLQ1U760GH9";
        String url3 = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + company.getCompanySymbol() + "&apikey=CV6LEEDA7KYE5T5N";
        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonMetaData = response.getJSONObject("Meta Data");
                            jsonPrices = response.getJSONObject("Time Series (5min)");

                            ArrayList<Price> n = new ArrayList<>();

                            if (jsonPrices != null) {
                                   for (int i = jsonPrices.names().length() - 1; i >= 0; i--){

                                    String key = jsonPrices.names().getString(i);
                                    JSONObject y = jsonPrices.getJSONObject(key);
                                    double open = Double.parseDouble(y.get("1. open").toString());
                                    double high = Double.parseDouble(y.get("2. high").toString());
                                    double low = Double.parseDouble(y.get("3. low").toString());
                                    double close = Double.parseDouble(y.get("4. close").toString());
                                    int volume = Integer.parseInt(y.get("5. volume").toString());

                                    Price price = new Price(key, open, high, low, close, volume);
                                    n.add(price);
                                }
                            }

                            company.setCompanyStockPrices(n);
                            currentPrice.setText(String.valueOf(company.getCompanyStockPrices().get(company.getCompanyStockPrices().size() - 1).getPriceClose()));
                            double priceChangeDouble = (company.getCompanyStockPrices().get(company.getCompanyStockPrices().size() - 100).getPriceClose() -
                                    company.getCompanyStockPrices().get(company.getCompanyStockPrices().size() - 1).getPriceClose()) /
                                    company.getCompanyStockPrices().get(company.getCompanyStockPrices().size() - 1).getPriceClose() * 100;

                            displayChart(company, chart);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "API on cool down! Please wait 1 minute", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, url2, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String jsonDescription = response.getString("Description");
                            String jsonSector = response.getString("Sector");
                            String jsonPeRatio = response.getString("PERatio");
                            String jsonPegRatio = response.getString("PEGRatio");
                            String jsonLastDividend = response.getString("DividendDate");
                            String jsonEmployees = response.getString("FullTimeEmployees");
                            String jsonAnalystTargetPrice = response.getString("AnalystTargetPrice");
                            String jsonPbRatio = response.getString("PriceToBookRatio");
                            String jsonpPayoutRatio = response.getString("PayoutRatio");

                            description.setText(jsonDescription);
                            sector.setText(jsonSector);
                            peRatio.setText(jsonPeRatio);
                            pegRatio.setText(jsonPegRatio);
                            lastDividend.setText(jsonLastDividend);
                            employees.setText(jsonEmployees);
                            analystTargetPrice.setText(jsonAnalystTargetPrice);
                            pbRatio.setText(jsonPbRatio);
                            payoutRatio.setText(jsonpPayoutRatio);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "API on cool down! Please wait 1 minute", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        JsonObjectRequest request3 = new JsonObjectRequest(Request.Method.GET, url3, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String jsonPriceChange = response.getJSONObject("Global Quote").getString("10. change percent");
                            jsonPriceChange = jsonPriceChange.replace("%", "");

                            double priceChangeDouble = Double.parseDouble(jsonPriceChange);

                            DecimalFormat df = new DecimalFormat("#.##");
                            String priceChangeString = df.format(priceChangeDouble) + "%";
                            if (priceChangeDouble > 0) {
                                priceChangeString = "+" + priceChangeString;
                                priceChange.setTextColor(Color.GREEN);
                            } else if (priceChangeDouble < 0) {
                                priceChange.setTextColor(Color.RED);
                            } else {
                                priceChange.setTextColor(Color.WHITE);
                            }
                            priceChange.setText(priceChangeString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "API on cool down! Please wait 1 minute", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request1);
        mQueue.add(request2);
        mQueue.add(request3);
    }

    public void displayChart(Company company, CandleStickChart chart) {
        ArrayList<CandleEntry> ceList = new ArrayList<>();
        ArrayList<String> c = new ArrayList<>();
        for (int i = 0; i < company.getCompanyStockPrices().size(); i++) {
            c.add(company.getCompanyStockPrices().get(i).getPriceDate());

            ceList.add(new CandleEntry(i, (float) company.getCompanyStockPrices().get(i).getPriceHigh(), (float) company.getCompanyStockPrices().get(i).getPriceLow(), (float) company.getCompanyStockPrices().get(i).getPriceOpen(), (float) company.getCompanyStockPrices().get(i).getPriceClose()));
        }
        chart.getXAxis().setGranularityEnabled(true);
        chart.getXAxis().setGranularity(1.0f);
        chart.getXAxis().setLabelCount(3);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(c));
        chart.setBackgroundColor(Color.BLACK);
        chart.getAxisLeft().setTextColor(Color.WHITE);
        chart.getXAxis().setTextColor(Color.WHITE);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getLegend().setEnabled(false);

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
        LimitLine ll = new LimitLine((float) company.getCompanyStockPrices().get(company.getCompanyStockPrices().size() - 1).getPriceClose()); // set where the line should be drawn
        ll.setLineColor(Color.GREEN);
        ll.setLineWidth(1f);
        chart.getAxisLeft().addLimitLine(ll);
        chart.setData(cd);
        chart.invalidate();
    }
}
