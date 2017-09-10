package com.udacity.stockhawk.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.udacity.stockhawk.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * Created by PH-Data™ 01221240053 on 02/04/2017.
 */



public class StockFrag extends Fragment {
    TextView mySympol;
    TextView myPrice;
    GraphView graph;
    String history;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        graph = (GraphView) getActivity().findViewById(R.id.graph);
        SharedPreferences.Editor e = sharedPref.edit();
        e.putInt("k", graph.getScrollX());
    }

    int sx;
    int sy;
    private static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.stockfrag, container, false);
        Bundle b = getActivity().getIntent().getBundleExtra("rawy");
        Stock myStock = (Stock) b.getSerializable("mystock");
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        mySympol = (TextView) getActivity().findViewById(R.id.symb);
        myPrice = (TextView) getActivity().findViewById(R.id.stockprice);
        mySympol.setText(myStock.getSympol());
        Locale current = getActivity().getResources().getConfiguration().locale;

       String fg= arabicToDecimal(String.valueOf(myStock.getPrice()));
          myStock.setPrice(Double.valueOf(fg));
        myPrice.setText(""+formatter.format(Double.valueOf(new DecimalFormat("##.##").format(myStock.getPrice()))));
        history = myStock.getHistory();
        String hisoryArray[] = history.split("[\n,]");
        ArrayList<String> timeArray = new ArrayList<>();
        ArrayList<String> priceArray = new ArrayList<>();
        for (int i = 0; i < hisoryArray.length; i++) {
            if (i % 2 == 0)
                timeArray.add(hisoryArray[i]);
            else {
                String format = new DecimalFormat("##.##").format(Double.valueOf(hisoryArray[i]));
                priceArray.add(format);
            }

        }

        DataPoint[] myPoints = new DataPoint[timeArray.size()];
        int count = myPoints.length;
        for (int i = 0; i < myPoints.length; i++) {
            Date d = new Date(Long.valueOf(timeArray.get(i)));
            myPoints[--count] = new DataPoint(d, Double.valueOf(priceArray.get(i)));
        }
        graph = (GraphView) v.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(myPoints);
        graph.addSeries(series);
// set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(2); // only 4 because of the space
// set manual x bounds to have nice steps
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(Long.valueOf(timeArray.get(timeArray.size()-1)));
        graph.getViewport().setMaxX(Long.valueOf(timeArray.get(0)));
        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        // graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScrollable(true);

        return v;
    }
}
