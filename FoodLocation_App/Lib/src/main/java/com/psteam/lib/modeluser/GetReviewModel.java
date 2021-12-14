package com.psteam.lib.modeluser;

import java.io.Serializable;
import java.util.ArrayList;

public class GetReviewModel implements Serializable{
    private String notification;

    private CountRating countRating;

    private ArrayList<Rate> rates;

    private String status;

    private String rateTotal;

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public CountRating getCountRating() {
        return countRating;
    }

    public void setCountRating(CountRating countRating) {
        this.countRating = countRating;
    }

    public ArrayList<Rate> getRates() {
        return rates;
    }

    public void setRates(ArrayList<Rate> rates) {
        this.rates = rates;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRateTotal() {
        return rateTotal;
    }

    public void setRateTotal(String rateTotal) {
        this.rateTotal = rateTotal;
    }

    public class CountRating implements Serializable {
        private String count1;

        private String count2;

        private String count5;

        private String count3;

        private String count4;

        private String count;

        public String getCount1() {
            return count1;
        }

        public void setCount1(String count1) {
            this.count1 = count1;
        }

        public String getCount2() {
            return count2;
        }

        public void setCount2(String count2) {
            this.count2 = count2;
        }

        public String getCount5() {
            return count5;
        }

        public void setCount5(String count5) {
            this.count5 = count5;
        }

        public String getCount3() {
            return count3;
        }

        public void setCount3(String count3) {
            this.count3 = count3;
        }

        public String getCount4() {
            return count4;
        }

        public void setCount4(String count4) {
            this.count4 = count4;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

    }


}

