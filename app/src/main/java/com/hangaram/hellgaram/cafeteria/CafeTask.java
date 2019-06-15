package com.hangaram.hellgaram.cafeteria;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hangaram.hellgaram.datebase.DataBaseHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CafeTask extends AsyncTask<Integer, Integer, Boolean> {
    private static final String TAG = "CafeTask";

    private Context mContext;

    private DataBaseHelper mDataBaseHelper;
    private SQLiteDatabase mDatabase;

    public CafeTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {

        mDataBaseHelper = new DataBaseHelper(mContext);
        mDatabase = mDataBaseHelper.getReadableDatabase();

        /*
         * 교육청 (ex. 서울 -> sen.go.kr/ )
         * 요청정보 (ex. 한 달치 급식정보 -> sts_sci_md00_001.do, 학교 스케줄 정보 -> sts_sci_sf01_001.do)
         * sculCode: 학교코드 (ex. 한가람고 -> B100000549)
         * schulCrseScCod: 학교종류 (ex. 고등학교 -> 4)
         * schulKndScCode: 학교종류 (ex. 고등학교 -> 04)
         * schYm: 요청하는 달 (ex. 201906)
         */

        String urlString = "https://stu."
                + "sen.go.kr/"
                + "sts_sci_md00_001.do" + "?"
                + "schulCode=B100000549"
                + "&schulCrseScCode=4"
                + "&schulKndScCode=04"
                + "&schYm=" + params[0] + String.format("%02d", params[1]);

        try {
            Document doc = Jsoup.connect(urlString).get();
            Elements tds = doc.getElementsByTag("td");
            Elements trs = doc.getElementsByTag("tr");

            int date = 1;

            Log.d(TAG, trs.toString());

            /*급식 정보가 교육청 서버에 있는지 확인하기
             * 급식 정보가 없을 때 trs.toString().trim().length() = 1060*/
            if (trs.toString().trim().length() > 2000) {

                /* 1 [중식] 토마토스파게티*1.4.6 크림스프5.6.9 ... [석식] 찹쌀밥 ... */
                for (Element td : tds) {
                    /* <td><div></div></td> 거르기*/
                    if (!td.text().equals("")) {
                        String[] spilts = td.text().split(" ");

                        boolean isLunch = false;
                        boolean isDinner = false;

                        StringBuilder lunchText = new StringBuilder();
                        StringBuilder dinnerText = new StringBuilder();

                        /*점심, 저녁 정보 가져오기*/
                        for (String split : spilts) {
                            if (split.contains("중식")) {
                                isLunch = true;
                                continue;
                            } else if (split.contains("석식")) {
                                isDinner = true;
                                continue;
                            }

                            if (isLunch && !isDinner) {
                                lunchText.append(filterAllergyInfo(split)).append("\n");
                            } else if (isLunch && isDinner) {
                                dinnerText.append(filterAllergyInfo(split)).append("\n");
                            }
                        }

                        if (lunchText.toString().equals(""))
                            lunchText.append("급식이 없어요~");

                        if (dinnerText.toString().equals(""))
                            dinnerText.append("급식이 없어요~");

                        Log.d(TAG, params[0] + "." + params[1] + "." + date + "\n" + "---중식---\n" + lunchText.toString() + "---석식---\n" + dinnerText.toString());

                        /*데이터베이스에 저장하기*/
                        ContentValues contentValues = new ContentValues();

                        contentValues.put("year", params[0]);
                        contentValues.put("month", params[1]);
                        contentValues.put("date", date++);
                        contentValues.put("lunch", lunchText.toString());
                        contentValues.put("dinner", dinnerText.toString());

                        mDatabase.insert(DataBaseHelper.TABLE_NAME_MEAL, null, contentValues);
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean isMeal) {
        super.onPostExecute(isMeal);

        if (!isMeal)
            Toast.makeText(mContext, "급식정보가 없습니다.\n 학교 영양사님이 급식표를 준비 중이실 수도 있습니다.", Toast.LENGTH_SHORT).show();
        else
            CafeFragment.getInstance().refresh();
    }

    /*알레르기 정보가 있으면 잘라서 반환하고 없으면 그대로 반환한다*/
    private String filterAllergyInfo(String split) {
        for (int index = 0; index < split.length(); index++) {
            if ((split.charAt(index) >= '1' && split.charAt(index) <= '9') || split.charAt(index) == '*') {
                return split.substring(0, index);
            }
        }
        return split;
    }
}
