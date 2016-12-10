package com.huangmb.idea.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by huangmb on 2016/12/10.
 */
public class TranslateResult {
    private final static String US_PHONETIC = "us-phonetic";
    private final static String UK_PHONETIC = "uk-phonetic";
    private final static String PHONETIC = "phonetic";
    private final static String EXPLAINS = "explains";

    private final static int SUCCESS = 0;
    private final static int QUERY_STRING_TOO_LONG = 20;
    private final static int CAN_NOT_TRANSLATE = 30;
    private final static int INVALID_LANGUAGE = 40;
    private final static int INVALID_KEY = 50;
    private final static int NO_RESULT = 60;


    /**
     * translation : ["好"]
     * basic : {"us-phonetic":"ɡʊd","phonetic":"gʊd","uk-phonetic":"gʊd","explains":["n. 好处；善行；慷慨的行为","adj. 好的；优良的；愉快的；虔诚的","adv. 好","n. (Good)人名；(英)古德；(瑞典)戈德"]}
     * query : good
     * errorCode : 0
     * web : [{"value":["好","善","商品"],"key":"Good"},{"value":["公共物品","公益事业","公共财"],"key":"public good"},{"value":["굿 닥터","Good Doctor (TV series)","好医生"],"key":"Good Doctor"}]
     */

    @SerializedName("basic")
    private BasicResult mBasicResult;//基本释义
    private String query;//请求单词
    private int errorCode;
    private List<String> translation;//有道翻译
    @SerializedName("web")
    private List<WebResult> mWebResults;//网络释义

    public BasicResult getBasicResult() {
        return mBasicResult;
    }

    public void setBasicResult(BasicResult basicResult) {
        this.mBasicResult = basicResult;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public List<WebResult> getWebResults() {
        return mWebResults;
    }

    public void setWebResults(List<WebResult> webResults) {
        this.mWebResults = webResults;
    }

    public static class BasicResult {
        /**
         * us-phonetic : ɡʊd
         * phonetic : gʊd
         * uk-phonetic : gʊd
         * explains : ["n. 好处；善行；慷慨的行为","adj. 好的；优良的；愉快的；虔诚的","adv. 好","n. (Good)人名；(英)古德；(瑞典)戈德"]
         */

        @SerializedName("us-phonetic")
        private String usPhonetic;//美式发音
        private String phonetic;
        @SerializedName("uk-phonetic")
        private String ukPhonetic;//英式发音
        private List<String> explains;//释义

        public String getUsPhonetic() {
            return usPhonetic;
        }

        public void setUsPhonetic(String usPhonetic) {
            this.usPhonetic = usPhonetic;
        }

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public String getUkPhonetic() {
            return ukPhonetic;
        }

        public void setUkPhonetic(String ukPhonetic) {
            this.ukPhonetic = ukPhonetic;
        }

        public List<String> getExplains() {
            return explains;
        }

        public void setExplains(List<String> explains) {
            this.explains = explains;
        }

        @Override
        public String toString() {
            String result = "";
            result += String.format("英式:[%s]; 美式:[%s]\n", getUkPhonetic(), getUsPhonetic());
            if (explains == null || explains.isEmpty()) {
                return result;
            }
            result += "\n本地释义:\n";
            for (String explain : explains) {
                result += explain + "\n";
            }
            return result;
        }
    }

    public static class WebResult {
        /**
         * value : ["好","善","商品"]
         * key : Good
         */

        private String key;
        private List<String> value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }

        @Override
        public String toString() {
            String result = "";
            String key = getKey();
            result += (key + " : ");
            List<String> value = getValue();
            for (String str : value) {
                result += (str + ",");
            }
            result += "\n";
            return result;
        }
    }

    private String getErrorMessage() {
        switch (errorCode) {
            case SUCCESS:
                return "成功";
            case QUERY_STRING_TOO_LONG:
                return "要翻译的文本过长";
            case CAN_NOT_TRANSLATE:
                return "无法进行有效的翻译";
            case INVALID_LANGUAGE:
                return "不支持的语言类型";
            case INVALID_KEY:
                return "无效的key";
            case NO_RESULT:
                return "无词典结果";
        }
        return "其他错误";
    }

    @Override
    public String toString() {
        if (errorCode != SUCCESS) {
            return "错误代码:" + errorCode + "\n" + getErrorMessage();
        }
        String string = query + ":\n";
        if (translation != null) {
            for (String r : translation) {
                string += r + "\n";
            }
        }

        if (mBasicResult != null) {
            string += mBasicResult.toString();
        }
        if (mWebResults != null) {
            string += "\n网络释义:\n";
            for (WebResult webResult : mWebResults) {
                string += webResult.toString();
            }
        }

        if ("".equals(string)) {
            string = "你选的内容:\"" + query + "\"抱歉,翻译不了...";
        }
        return string;
    }

}
