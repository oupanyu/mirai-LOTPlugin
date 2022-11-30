package top.oupanyu.Functions.Bilibili;

import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import top.oupanyu.Functions.Bilibili.Exception.NoSuchVideoVideoException;
import top.oupanyu.Functions.Bilibili.Exception.WrongBVIDTypeException;
import top.oupanyu.request.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

public class GetBVideoInfo {
    public static void getbyAID(MessageChain chain, GroupMessageEvent event) throws NumberFormatException{
        try {
            Integer avid = Integer.parseInt(chain.contentToString().replace(".B站 ",""));
            String result = Request.get(String.format("http://api.bilibili.com/x/web-interface/view?aid=%s",avid));//GET BilibiliAPI
            JSONObject obj = JSONObject.parseObject(result);
            if (obj.getInteger("code") == 40003){//检测是否存在该视频，如果无，抛出异常
                throw new NoSuchVideoVideoException();
            }
            Integer views,danmaku,likes;
            String title,owner;

            views = obj.getJSONObject("data").getJSONObject("stat").getInteger("view");
            danmaku = obj.getJSONObject("data").getJSONObject("stat").getInteger("danmaku");
            likes = obj.getJSONObject("data").getJSONObject("stat").getInteger("like");
            title = obj.getJSONObject("data").getString("title");
            owner = obj.getJSONObject("data").getJSONObject("owner").getString("name");

            event.getSubject().sendMessage(String.format("视频：%s\n拥有者：%s\n观看数：%s\n点赞数：%s\n弹幕数：%s",title,owner,views,likes,danmaku));

            //event.getSubject().sendMessage(result);

        } catch (NoSuchVideoVideoException e) {
            event.getSubject().sendMessage("出现错误！没有" + chain.contentToString().replace(".B站 ","") + "这个视频！");
        }/*catch (NumberFormatException e){
            event.getSubject().sendMessage("出现错误！\n" + e.getMessage());
        }*/
    }

    public static void getByBID(MessageChain chain,GroupMessageEvent event) {
        String LETTER_DIGIT_REGEX = "^[a-z0-9A-Z]+$";

        String bvid = chain.contentToString().replace(".B站 ","");

        if(!bvid.matches(LETTER_DIGIT_REGEX)){
            try {
                throw new WrongBVIDTypeException();
            } catch (WrongBVIDTypeException e) {
                event.getSubject().sendMessage("出现错误！没有" + bvid + "这个视频！");
            }
        }
        try{
        String result = Request.get(String.format("http://api.bilibili.com/x/web-interface/view?bvid=%s",bvid));//GET BilibiliAPI
        //String result = "{\"code\":0,\"message\":\"0\",\"ttl\":1,\"data\":{\"bvid\":\"BV1aP411u7MA\",\"aid\":305560844,\"videos\":1,\"tid\":138,\"tname\":\"搞笑\",\"copyright\":1,\"pic\":\"http://i1.hdslb.com/bfs/archive/2f035d569584f1a3fdbad7eddf43bead80ee93ce.jpg\",\"title\":\"央视依旧稳定发挥\",\"pubdate\":1669516151,\"ctime\":1669516152,\"desc\":\"-\",\"desc_v2\":[{\"raw_text\":\"-\",\"type\":1,\"biz_id\":0}],\"state\":0,\"duration\":11,\"rights\":{\"bp\":0,\"elec\":0,\"download\":1,\"movie\":0,\"pay\":0,\"hd5\":0,\"no_reprint\":0,\"autoplay\":1,\"ugc_pay\":0,\"is_cooperation\":0,\"ugc_pay_preview\":0,\"no_background\":0,\"clean_mode\":0,\"is_stein_gate\":0,\"is_360\":0,\"no_share\":0,\"arc_pay\":0,\"free_watch\":0},\"owner\":{\"mid\":628270438,\"name\":\"本猪累了\",\"face\":\"https://i1.hdslb.com/bfs/face/5b412ce635efb05fce5928283877c551e6b667b0.jpg\"},\"stat\":{\"aid\":305560844,\"view\":320991,\"danmaku\":19,\"reply\":141,\"favorite\":928,\"coin\":96,\"share\":1402,\"now_rank\":0,\"his_rank\":0,\"like\":11160,\"dislike\":0,\"evaluation\":\"\",\"argue_msg\":\"\"},\"dynamic\":\"\",\"cid\":904328630,\"dimension\":{\"width\":1440,\"height\":1080,\"rotate\":0},\"premiere\":null,\"teenage_mode\":0,\"is_chargeable_season\":false,\"is_story\":false,\"no_cache\":false,\"pages\":[{\"cid\":904328630,\"page\":1,\"from\":\"vupload\",\"part\":\"央视依旧稳定发挥\",\"duration\":11,\"vid\":\"\",\"weblink\":\"\",\"dimension\":{\"width\":1440,\"height\":1080,\"rotate\":0},\"first_frame\":\"http://i1.hdslb.com/bfs/storyff/n221127qn2pblppbphba5z1rip1zs11g_firsti.jpg\"}],\"subtitle\":{\"allow_submit\":false,\"list\":[{\"id\":1102689631255960320,\"lan\":\"ai-zh\",\"lan_doc\":\"中文（自动生成）\",\"is_lock\":false,\"subtitle_url\":\"http://i0.hdslb.com/bfs/ai_subtitle/prod/305560844904328630ba3bc533f75978d99ee1eb6ed06cfbf4\",\"type\":1,\"id_str\":\"1102689631255960320\",\"ai_type\":0,\"ai_status\":2,\"author\":{\"mid\":0,\"name\":\"\",\"sex\":\"\",\"face\":\"\",\"sign\":\"\",\"rank\":0,\"birthday\":0,\"is_fake_account\":0,\"is_deleted\":0,\"in_reg_audit\":0,\"is_senior_member\":0}}]},\"is_season_display\":false,\"user_garb\":{\"url_image_ani_cut\":\"\"},\"honor_reply\":{},\"like_icon\":\"\"}}";
        JSONObject obj = JSONObject.parseObject(result);
        if (obj.getInteger("code") == 40003){//检测是否存在该视频，如果无，抛出异常
            throw new NoSuchVideoVideoException();
        }
        Integer views,danmaku,likes;
        String title,owner;
        views = obj.getJSONObject("data").getJSONObject("stat").getInteger("view");
        danmaku = obj.getJSONObject("data").getJSONObject("stat").getInteger("danmaku");
        likes = obj.getJSONObject("data").getJSONObject("stat").getInteger("like");
        title = obj.getJSONObject("data").getString("title");
        owner = obj.getJSONObject("data").getJSONObject("owner").getString("name");

        event.getSubject().sendMessage(String.format("视频：%s\n拥有者：%s\n观看数：%s\n点赞数：%s\n弹幕数：%s",title,owner,views,likes,danmaku));

        //event.getSubject().sendMessage(result);

        } catch (NoSuchVideoVideoException e) {
            event.getSubject().sendMessage("出现错误！没有" + chain.contentToString().replace(".B站 ","") + "这个视频！");
        }catch (NumberFormatException e){
            event.getSubject().sendMessage("出现错误！\n" + e.getMessage());
        }catch (Exception e){
            event.getSubject().sendMessage(e.getMessage());
        }



    }
}
