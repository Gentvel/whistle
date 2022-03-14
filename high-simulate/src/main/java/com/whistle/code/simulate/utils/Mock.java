package com.whistle.code.simulate.utils;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.script.ScriptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
public class Mock {

    /**
     * Javascript执行引擎
     */
    public static final ScriptEngine MOCK_JS_ENGINE;
    /**
     * mockjs的资源路径
     */
    private static final String MOCK_JS_PATH = "src/main/resources/mock.js";

    /*
     * 初始化
     */
    static {
        MOCK_JS_ENGINE = ScriptUtil.getJavaScriptEngine();
        File file = new File(MOCK_JS_PATH);
        String mockjsString = FileUtil.readUtf8String(file);
        if (StringUtils.hasText(mockjsString)) {
            log.debug("读取MockJs成功！");
        }
        try {
            MOCK_JS_ENGINE.eval(mockjsString);
        } catch (ScriptException e) {
            log.error("初始化MockJs失败，出现错误：",e);
        }
    }

    private static Object mock(String sentences) {
        if (!StringUtils.hasText(sentences)) {
            throw new RuntimeException("输入语句不能为空");
        }
        try {
            return MOCK_JS_ENGINE.eval(String.format("Mock.%s", sentences));
        } catch (ScriptException e) {
            log.error("脚本执行错误:", e);
            return new Object();
        }
    }

    /**
     * 生成Boolean
     */
    public static boolean bool() {
        Object random = mock("Random.boolean()");
        return Convert.toBool(random);
    }

    /**
     * 生成一个大于0的随机数
     */
    public static long natural() {
        Object random = mock("Random.natural()");
        return Convert.toLong(random);
    }

    /**
     * 生成区间随机数
     * @param max max
     */
    public static long natural(long max)  {
        String format = StrFormatter.format("Random.natural({},{})",1, max);
        Object random = mock(format);
        return Convert.toLong(random);
    }

    /**
     * 生成区间随机数
     * @param min 最小值
     * @param max 最大值
     */
    public static long natural(long min, long max){
        if(max<min){
            throw new IllegalArgumentException("max value must greater then min");
        }
        String format = StrFormatter.format("Random.natural({},{})", min, max);
        Object random = mock(format);
        return Convert.toLong(random);
    }

    /**
     * 生成一个随机字符
     */
    public static Character character() {
        Object random = mock("Random.character()");
        return Convert.toChar(random);
    }

    /**
     * 生成一个随机日期字符串
     */
    public static String date() {
        String format = StrFormatter.format("Random.date()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 返回一个随机的日期字符串 (带格式)
     */
    public static String date(String dateFormat) {
        String format = StrFormatter.format("Random.date('{}')", dateFormat);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 返回一个随机的时间字符串。
     */
    public static String time() {
        String format = StrFormatter.format("Random.time()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：返回一个随机的时间字符串。
     * 
     */
    public static String time(String timeFormat) {
        String format = StrFormatter.format("Random.time('{}')", timeFormat);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：返回一个随机的日期和时间字符串。
     *
     */
    public static String datetime() {
        String format = StrFormatter.format("Random.datetime()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：返回一个随机的日期和时间字符串。
     *
     */
    public static String datetime(String datetimeFormat) {
        String format = StrFormatter.format("Random.datetime('{}')", datetimeFormat);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：生成一个随机的图片地址。
     *
     */
    public static String image() {
        String format = StrFormatter.format("Random.image()");
        Object random = mock(format);
        return URLEncoder.encode(Convert.toStr(random), StandardCharsets.UTF_8);
    }

    /**
     * 方法描述：生成一个随机的图片地址。
     * @param size 指示图片的宽高，格式为 '宽x高'。例如：“300x250”，”360x123“
     *
     */
    public static String image(String size) {
        String format = StrFormatter.format("Random.image('{}')", size);
        Object random = mock(format);
        return URLEncoder.encode(Convert.toStr(random), StandardCharsets.UTF_8);
    }

    /**
     * 方法描述：生成一个随机的图片地址。
     * @param size       指示图片的宽高，格式为 '宽x高'。例如：“300x250”，”360x123“
     * @param background 指示图片的背景色。默认值为 '#000000'。
     * 
     */
    public static String image(String size, String background) {
        String format = StrFormatter.format("Random.image('{}','{}')", size, background);
        Object random = mock(format);
        return URLEncoder.encode(Convert.toStr(random), StandardCharsets.UTF_8);
    }

    /**
     * 方法描述：生成一个随机的图片地址。
     *
     * @param size       指示图片的宽高，格式为 '宽x高'。例如：“300x250”，”360x123“
     * @param background 指示图片的背景色。默认值为 '#000000'。
     * @param text       指示图片上的文字。默认值为参数 size。
     * 
     */
    public static String image(String size, String background, String text) {
        String format = StrFormatter.format("Random.image('{}','{}','{}')", size, background, text);
        Object random = mock(format);
        return URLEncoder.encode(Convert.toStr(random), StandardCharsets.UTF_8);
    }

    /**
     * 方法描述：生成一个随机的图片地址。
     *
     * @param size       指示图片的宽高，格式为 '宽x高'。例如：“300x250”，”360x123“
     * @param background 指示图片的背景色。默认值为 '#000000'。
     * @param foreground 指示图片的前景色（文字）。默认值为 '#FFFFFF'。
     * @param text       指示图片上的文字。默认值为参数 size。
     * 
     */
    public static String image(String size, String background, String foreground, String text) {
        String format = StrFormatter.format("Random.image('{}','{}','{}','{}')", size, background, foreground, text);
        Object random = mock(format);
        return URLEncoder.encode(Convert.toStr(random), StandardCharsets.UTF_8);
    }

    /**
     * 方法描述：生成一个随机的图片地址。
     *
     * @param size       指示图片的宽高，格式为 '宽x高'。例如：“300x250”，”360x123“
     * @param background 指示图片的背景色。默认值为 '#000000'。
     * @param foreground 指示图片的前景色（文字）。默认值为 '#FFFFFF'。
     * @param imgFormat  指示图片的格式。默认值为 'png'，可选值包括：'png'、'gif'、'jpg'。
     * @param text       指示图片上的文字。默认值为参数 size。
     * 
     */
    public static String image(String size, String background, String foreground, String imgFormat, String text) {
        String format = StrFormatter.format("Random.image('{}','{}','{}','{}','{}')", size, background, foreground, imgFormat, text);
        Object random = mock(format);
        return URLEncoder.encode(Convert.toStr(random), StandardCharsets.UTF_8);
    }

    /**
     * 方法描述：随机生成一个有吸引力的颜色，格式为 '#RRGGBB'。
     */
    public static String color() {
        String format = StrFormatter.format("Random.color()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个有吸引力的颜色，格式为 '#RRGGBB'。
     *
     */
    public static String hex() {
        String format = StrFormatter.format("Random.hex()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个有吸引力的颜色，格式为 'rgb(r, g, b)'。
     *
     */
    public static String rgb() {
        String format = StrFormatter.format("Random.rgb()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个有吸引力的颜色，格式为 'rgba(r, g, b, a)'。
     *
     */
    public static String rgba() {
        String format = StrFormatter.format("Random.rgba()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个有吸引力的颜色，格式为 'hsl(h, s, l)'。
     *
     */
    public static String hsl() {
        String format = StrFormatter.format("Random.hsl()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一段英文文本。
     *
     */
    public static String paragraph() {
        String format = StrFormatter.format("Random.paragraph()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一段英文文本。
     * @param len 指示文本中句子的个数。默认值为 3 到 7 之间的随机数。
     * 
     */
    public static String paragraph(int len) {
        String format = StrFormatter.format("Random.paragraph({})", len);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一段英文文本。
     * @param min 指示文本中句子的最小个数。默认值为 3。
     * @param max 指示文本中句子的最大个数。默认值为 7。
     * 
     */
    public static String paragraph(int min, int max) {
        String format = StrFormatter.format("Random.paragraph({},{})", min, max);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一段中文文本。
     *
     */
    public static String cparagraph() {
        String format = StrFormatter.format("Random.cparagraph()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一段中文文本。
     * @param len 指示文本中句子的个数。默认值为 3 到 7 之间的随机数。
     * 
     */
    public static String cparagraph(int len) {
        String format = StrFormatter.format("Random.cparagraph({})", len);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一段中文文本。
     * @param min 指示文本中句子的最小个数。默认值为 3。
     * @param max 指示文本中句子的最大个数。默认值为 7。
     * 
     */
    public static String cparagraph(int min, int max) {
        String format = StrFormatter.format("Random.cparagraph({},{})", min, max);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个单词。
     *
     */
    public static String word() {
        String format = StrFormatter.format("Random.word()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个单词。
     * @param len 指示单词中字符的个数。默认值为 3 到 10 之间的随机数。
     * 
     */
    public static String word(int len) {
        String format = StrFormatter.format("Random.word({})", len);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个单词。
     * @param min 指示单词中字符的最小个数。默认值为 3。
     * @param max 指示单词中字符的最大个数。默认值为 10。
     * 
     */
    public static String word(int min, int max) {
        String format = StrFormatter.format("Random.word({},{})", min, max);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个汉字。
     *
     */
    public static String cword() {
        String format = StrFormatter.format("Random.cword()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个汉字。
     * @param pool 汉字字符串。表示汉字字符池，将从中选择一个汉字字符返回。
     * 
     */
    public static String cword(String pool) {
        String format = StrFormatter.format("Random.cword('{}')", pool);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成 length 个汉字。
     * @param length 随机字符串的长度
     * 
     */
    public static String cword(int length) {
        String format = StrFormatter.format("Random.cword({})", length);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成汉字。
     * @param min 随机汉字字符串的最小长度。默认值为 1。
     * @param max 随机汉字字符串的最大长度。默认值为 1。
     * 
     */
    public static String cword(int min, int max) {
        String format = StrFormatter.format("Random.cword({},{})", min, max);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成汉字。
     * @param pool 汉字字符串。表示汉字字符池，将从中选择一个汉字字符返回。
     * @param min  随机汉字字符串的最小长度。默认值为 1。
     * @param max  随机汉字字符串的最大长度。默认值为 1。
     * 
     */
    public static String cword(String pool, int min, int max) {
        String format = StrFormatter.format("Random.cword('{}',{},{})", pool, min, max);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一句标题，其中每个单词的首字母大写。
     *
     */
    public static String title() {
        String format = StrFormatter.format("Random.title()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一句标题，其中每个单词的首字母大写。
     * @param len 指示单词中字符的个数。默认值为 3 到 7 之间的随机数。
     * 
     */
    public static String title(int len) {
        String format = StrFormatter.format("Random.title({})", len);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一句标题，其中每个单词的首字母大写。
     * @param min 指示单词中字符的最小个数。默认值为 3。
     * @param max 指示单词中字符的最大个数。默认值为 7。
     * 
     */
    public static String title(int min, int max) {
        String format = StrFormatter.format("Random.title({},{})", min, max);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一句中文标题。
     *
     */
    public static String ctitle() {
        String format = StrFormatter.format("Random.ctitle()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一句中文标题。
     * @param len 指示单词中字符的个数。默认值为 3 到 7 之间的随机数。
     * 
     */
    public static String ctitle(int len) {
        String format = StrFormatter.format("Random.ctitle({})", len);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一句中文标题。
     * @param min 指示单词中字符的最小个数。默认值为 3。
     * @param max 指示单词中字符的最大个数。默认值为 7。
     * 
     */
    public static String ctitle(int min, int max) {
        String format = StrFormatter.format("Random.ctitle({},{})", min, max);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个常见的英文名。
     *
     */
    public static String first() {
        String format = StrFormatter.format("Random.first()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个常见的英文姓。
     *
     */
    public static String last() {
        String format = StrFormatter.format("Random.last()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个常见的英文姓名。
     *
     */
    public static String name() {
        String format = StrFormatter.format("Random.name()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个常见的英文姓名。
     * @param middle 指示是否生成中间名。
     * 
     */
    public static String name(boolean middle) {
        String format = StrFormatter.format("Random.name({})", middle);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个常见的中文名。
     *
     */
    public static String cfirst() {
        String format = StrFormatter.format("Random.cfirst()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个常见的中文姓。
     *
     * 
     */
    public static String clast() {
        String format = StrFormatter.format("Random.clast()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个常见的中文姓名。
     *
     */
    public static String cname() {
        String format = StrFormatter.format("Random.cname()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个 URL。
     *
     */
    public static String url() {
        String format = StrFormatter.format("Random.url()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个 URL。
     *
     * @param protocol 指定 URL 协议。例如 http。
     * @param host     指定 URL 域名和端口号。例如 nuysoft.com。
     * 
     */
    public static String url(String protocol, String host) {
        String format = StrFormatter.format("Random.url('{}','{}')", protocol, host);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个 URL 协议。返回以下值之一：
     * 'http'、'ftp'、'gopher'、'mailto'、'mid'、'cid'、'news'、'nntp'、
     * 'prospero'、'telnet'、'rlogin'、'tn3270'、'wais'。
     *
     */
    public static String protocol() {
        String format = StrFormatter.format("Random.protocol()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个域名。
     *
     */
    public static String domain() {
        String format = StrFormatter.format("Random.domain()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个顶级域名（Top Level Domain）。
     *
     */
    public static String tld() {
        String format = StrFormatter.format("Random.tld()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个邮件地址。
     *
     */
    public static String email() {
        String format = StrFormatter.format("Random.email()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个邮件地址。
     *
     */
    public static String email(String domain) {
        String format = StrFormatter.format("Random.email('{}')", domain);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个 IP 地址。
     *
     */
    public static String ip() {
        String format = StrFormatter.format("Random.ip()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个（中国）大区。
     *
     */
    public static String region() {
        String format = StrFormatter.format("Random.region()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个（中国）省（或直辖市、自治区、特别行政区）。
     *
     */
    public static String province() {
        String format = StrFormatter.format("Random.province()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个（中国）市。
     *
     */
    public static String city() {
        String format = StrFormatter.format("Random.city()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个（中国）市。
     * @param prefix 指示是否生成所属的省。
     * 
     */
    public static String city(boolean prefix) {
        String format = StrFormatter.format("Random.city({})", prefix);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个（中国）县。
     *
     */
    public static String county() {
        String format = StrFormatter.format("Random.county()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个（中国）县。
     * @param prefix 指示是否生成所属的省。
     * 
     */
    public static String county(boolean prefix) {
        String format = StrFormatter.format("Random.county({})", prefix);
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个邮政编码（六位数字）。
     *
     */
    public static String zip() {
        String format = StrFormatter.format("Random.zip()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    /**
     * 方法描述：随机生成一个 18 位身份证。
     *
     */
    public static String id() {
        String format = StrFormatter.format("Random.id()");
        Object random = mock(format);
        return Convert.toStr(random);
    }

    
}
