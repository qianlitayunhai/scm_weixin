/**
 * 通过模板生成Blh 
 * template by zxx
 */
package com.dhcc.scm.blh.weixin;

import javax.annotation.Resource;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;

import org.springframework.stereotype.Component;

import com.dhcc.framework.app.blh.AbstractBaseBlh;
import com.dhcc.framework.app.service.CommonService;
import com.dhcc.framework.transmission.event.BusinessRequest;
import com.dhcc.scm.dto.weixin.MpInGdRecDto;
import com.dhcc.scm.entity.userManage.NormalAccount;
import com.dhcc.scm.entity.vo.weixin.WxJsapiSign;

@Component
public class MpInGdRecBlh extends AbstractBaseBlh {

	@Resource
	private CommonService commonService;

	@Resource
	private WxMpService wxMpService;
	
	@Resource
	private WxMpConfigStorage wxMpConfigStorage;

	public MpInGdRecBlh() {

	}
	
	public String mpInGdRec(BusinessRequest res) {
		
		NormalAccount normalAccount=super.getMpUserId();
		if(normalAccount==null){
			return "mpSubscribe";
		}
		if(normalAccount.getNormalUser().getType().longValue()!=1){
			return "noPermission";
		}
		return "mpInGdRec";
	}
	
	
	
	public void getWxJsapiSignature(BusinessRequest res){
		MpInGdRecDto dto = super.getDto(MpInGdRecDto.class, res);
		WxJsapiSign jsapiSign=new WxJsapiSign();
		try {
			WxJsapiSignature jsapiSignature= wxMpService.createJsapiSignature(dto.getUrl());
			jsapiSign.setResultCode(0);
			jsapiSign.setAppId(wxMpConfigStorage.getAppId());
			jsapiSign.setNoncestr(jsapiSignature.getNoncestr());
			jsapiSign.setSignature(jsapiSignature.getSignature());
			jsapiSign.setTimestamp(jsapiSignature.getTimestamp());
		} catch (WxErrorException e) {
			e.printStackTrace();
			jsapiSign.setErrMsg(e.getMessage());
		}finally{
			super.writeJSON(jsapiSign);
		}
		
	}
}
