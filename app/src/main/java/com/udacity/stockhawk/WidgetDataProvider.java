package com.udacity.stockhawk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.data.Contract;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * WidgetDataProvider acts as the adapter for the collection view widget,
 * providing RemoteViews to the widget in the getViewAt method.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "WidgetDataProvider";

    List<String> sSymbols = new ArrayList<>();
    List<String> sPrices = new ArrayList<>();
    List<String> sChanges = new ArrayList<>();
    Cursor data;


    Context mContext = null;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return data.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                R.layout.widget);
        String price = sPrices.get(position).toString();
        String symbol = sSymbols.get(position).toString();
        String changed = sChanges.get(position).toString();
        view.setTextViewText(R.id.symbol1, symbol);
        view.setTextViewText(R.id.price1, price);
        view.setTextViewText(R.id.change1, changed);

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {
        DecimalFormat dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormat percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
        sSymbols.clear();
        sChanges.clear();
        sPrices.clear();
        String projection[] = {
                Contract.Quote.COLUMN_SYMBOL,
                Contract.Quote.COLUMN_PRICE,
                Contract.Quote.COLUMN_ABSOLUTE_CHANGE



        };
        data = mContext.getContentResolver().query(Contract.Quote.URI, projection, null, null, null);
        while (data.moveToNext()) {
            String symbol = data.getString(data.getColumnIndex(Contract.Quote.COLUMN_SYMBOL));
            Float price = data.getFloat(data.getColumnIndex(Contract.Quote.COLUMN_PRICE));
            Float priceChanged = data.getFloat(data.getColumnIndex(Contract.Quote.COLUMN_ABSOLUTE_CHANGE));
            sPrices.add(dollarFormat.format(price));
            sSymbols.add(symbol);
            sChanges.add(dollarFormat.format(priceChanged ));


        }
        data.close();
    }

}
