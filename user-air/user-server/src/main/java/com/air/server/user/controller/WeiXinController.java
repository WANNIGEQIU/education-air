package com.air.server.user.controller;

public class WeiXinController {

//    /**
//     * 获取微信二维码
//     */
//    @GetMapping("/login")
//    public String getQRcode(HttpSession session) {
//
//        String url = "https://open.weixin.qq.com/connect/qrconnect" +
//                "?appid=%s" +
//                "&redirect_uri=%s" +
//                "&response_type=code" +
//                "&scope=snsapi_login" +
//                "&state=%s" +
//                "#wechat_redirect";
//
//        // 回调地址
//        String redirectUrl = ConstantProperties.WEIXIN_REDIRECTURL;
//        try {
//
//            String encode = URLEncoder.encode(redirectUrl,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        String state = "heimao";
//        // 拼接二维码地址
//        String qrUrl = String.format(
//                url,ConstantProperties.WEIXIN_APPLEID,
//                redirectUrl,
//                state
//        );
//
//        return "redirect:"+qrUrl;
//    }



}
