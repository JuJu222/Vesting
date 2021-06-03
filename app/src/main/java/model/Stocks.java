package model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Stocks {
    JSONObject jsonMetaData;
    JSONObject jsonPrices;

    public void setCompanyChart(Context context, RequestQueue mQueue, ListedCompany listedCompany, CandleStickChart chart, TextView companyName,
                                   TextView currentPrice, TextView priceChange, TextView description, TextView sector, TextView peRatio,
                                   TextView pegRatio, TextView lastDividend, TextView employees, TextView analystTargetPrice,
                                   TextView pbRatio, TextView payoutRatio, Button button) {
        String url1 = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + listedCompany.getCompanySymbol() + "&interval=5min&outputsize=full&apikey=34V7BYNBX3LSQ79T";
        String url2 = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" + listedCompany.getCompanySymbol() + "&apikey=34V7BYNBX3LSQ79T";

        button.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        button.setBackgroundColor(Color.parseColor("#808080"));
        button.setEnabled(false);

        DecimalFormat df = new DecimalFormat("#.##");
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
                                    open = Double.parseDouble(df.format(open));
                                    double high = Double.parseDouble(y.get("2. high").toString());
                                    high = Double.parseDouble(df.format(high));
                                    double low = Double.parseDouble(y.get("3. low").toString());
                                    low = Double.parseDouble(df.format(low));
                                    double close = Double.parseDouble(y.get("4. close").toString());
                                    close = Double.parseDouble(df.format(close));
                                    int volume = Integer.parseInt(y.get("5. volume").toString());
                                    volume = Integer.parseInt(df.format(volume));

                                    Price price = new Price(key, open, high, low, close, volume);
                                    n.add(price);
                                }
                            }

                            listedCompany.setCompanyStockPrices(n);

                            double priceClose = listedCompany.getCompanyStockPrices().get(listedCompany.getCompanyStockPrices().size() - 1).getPriceClose();
                            double priceOld = listedCompany.getCompanyStockPrices().get(listedCompany.getCompanyStockPrices().size() - 101).getPriceClose();

                            currentPrice.setText(df.format(priceClose));
                            double priceChangeDouble = (priceClose - priceOld) / priceOld * 100;

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

                            displayChart(listedCompany, chart);
                            button.getBackground().setColorFilter(null);
                            button.setBackgroundColor(Color.parseColor("#41A03A"));
                            button.setEnabled(true);
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
                            String jsonCompanyName = response.getString("Name");
                            String jsonDescription = response.getString("Description");
                            String jsonSector = response.getString("Sector");
                            String jsonPeRatio = response.getString("PERatio");
                            String jsonPegRatio = response.getString("PEGRatio");
                            String jsonLastDividend = response.getString("DividendDate");
                            String jsonEmployees = response.getString("FullTimeEmployees");
                            String jsonAnalystTargetPrice = response.getString("AnalystTargetPrice");
                            String jsonPbRatio = response.getString("PriceToBookRatio");
                            String jsonpPayoutRatio = response.getString("PayoutRatio");

                            companyName.setText(jsonCompanyName);
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

        mQueue.add(request1);
        mQueue.add(request2);
    }

    public void setCompanyChartHome(Context context, RequestQueue mQueue, ListedCompany listedCompany, CandleStickChart chart,
                                TextView currentPrice, TextView priceChange) {
        String url1 = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + listedCompany.getCompanySymbol() + "&interval=5min&apikey=34V7BYNBX3LSQ79T";
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

                            listedCompany.setCompanyStockPrices(n);

                            double priceClose = listedCompany.getCompanyStockPrices().get(listedCompany.getCompanyStockPrices().size() - 1).getPriceClose();
                            double priceOld = listedCompany.getCompanyStockPrices().get(0).getPriceClose();

                            DecimalFormat df = new DecimalFormat("#.##");
                            currentPrice.setText(df.format(priceClose));
                            double priceChangeDouble = (priceClose - priceOld) / priceOld * 100;

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

                            displayChart(listedCompany, chart);
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
    }

    public void displayChart(ListedCompany listedCompany, CandleStickChart chart) {
        ArrayList<CandleEntry> ceList = new ArrayList<>();
        ArrayList<String> c = new ArrayList<>();
        for (int i = 0; i < listedCompany.getCompanyStockPrices().size(); i++) {
            c.add(listedCompany.getCompanyStockPrices().get(i).getPriceDate());

            ceList.add(new CandleEntry(i, (float) listedCompany.getCompanyStockPrices().get(i).getPriceHigh(), (float) listedCompany.getCompanyStockPrices().get(i).getPriceLow(), (float) listedCompany.getCompanyStockPrices().get(i).getPriceOpen(), (float) listedCompany.getCompanyStockPrices().get(i).getPriceClose()));
        }
        chart.getXAxis().setGranularityEnabled(true);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setLabelCount(2);
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
        cds.setIncreasingColor(Color.GREEN);
        cds.setIncreasingPaintStyle(Paint.Style.FILL);
        cds.setNeutralColor(Color.BLUE);
        cds.setValueTextColor(Color.RED);
        cds.setDrawValues(false);
        CandleData cd = new CandleData(cds);
        LimitLine ll = new LimitLine((float) listedCompany.getCompanyStockPrices().get(listedCompany.getCompanyStockPrices().size() - 1).getPriceClose()); // set where the line should be drawn
        ll.setLineColor(Color.GREEN);
        ll.setLineWidth(1f);
        chart.getAxisLeft().addLimitLine(ll);
        chart.setData(cd);
        chart.invalidate();
    }

    public void getCurrentPrice(RequestQueue mQueue, String companySymbol, final VolleyCallback callback) {
        String url1 = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + companySymbol + "&interval=5min&outputsize=full&apikey=34V7BYNBX3LSQ79T";
        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonPrices = response.getJSONObject("Time Series (5min)");
                            String key = jsonPrices.names().getString(0);
                            JSONObject y = jsonPrices.getJSONObject(key);

                            double close = Double.parseDouble(y.get("4. close").toString());

                            DecimalFormat df = new DecimalFormat("#.##");
                            callback.onSuccess(df.format(close));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request1);
    }
}
