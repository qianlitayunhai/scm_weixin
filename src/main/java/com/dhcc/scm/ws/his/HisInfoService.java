package com.dhcc.scm.ws.his;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.jws.WebService;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.dhcc.framework.app.service.CommonService;
import com.dhcc.framework.util.JsonUtils;
import com.dhcc.scm.blh.hop.HopCtlocBlh;
import com.dhcc.scm.blh.hop.HopIncBlh;
import com.dhcc.scm.blh.hop.HopIncLocBlh;
import com.dhcc.scm.blh.hop.HopVendorBlh;
import com.dhcc.scm.blh.ord.OrdBlh;
import com.dhcc.scm.blh.ord.OrderBlh;
import com.dhcc.scm.entity.hop.HopCtloc;
import com.dhcc.scm.entity.hop.HopInc;
import com.dhcc.scm.entity.ord.OrderDetail;
import com.dhcc.scm.entity.ord.OrderDetailPic;
import com.dhcc.scm.entity.ord.OrderDetailSub;
import com.dhcc.scm.entity.st.StInGdRecItm;
import com.dhcc.scm.entity.sys.SysLog;
import com.dhcc.scm.entity.userManage.NormalAccount;
import com.dhcc.scm.entity.ven.Vendor;
import com.dhcc.scm.entity.vo.ws.FileWrapper;
import com.dhcc.scm.entity.vo.ws.HisCmpRecWeb;
import com.dhcc.scm.entity.vo.ws.HisInGdRec;
import com.dhcc.scm.entity.vo.ws.HisInGdRecItm;
import com.dhcc.scm.entity.vo.ws.HisIncLocQtyWeb;
import com.dhcc.scm.entity.vo.ws.HisIncWeb;
import com.dhcc.scm.entity.vo.ws.HisInvInfoWeb;
import com.dhcc.scm.entity.vo.ws.HisLocWeb;
import com.dhcc.scm.entity.vo.ws.HisOrderWeb;
import com.dhcc.scm.entity.vo.ws.HisOrderWebVo;
import com.dhcc.scm.entity.vo.ws.HisPicWeb;
import com.dhcc.scm.entity.vo.ws.HisVendorWeb;
import com.dhcc.scm.entity.vo.ws.OperateResult;

/**
 * @author auto-generated by AnnoParameterParser
 * @timestamp 2014-07-10 09:36:34.205
 */
@WebService(endpointInterface = "com.dhcc.scm.ws.his.HisInfoServiceInterface",targetNamespace="http://his.ws.scm.dhcc.com/",
portName="HisInfoServiceServiceHttpSoap12Endpoint")
public class HisInfoService implements HisInfoServiceInterface{
	

	 @Resource
	 private OrderBlh blh;
	 
	 @Resource
	 private HopIncLocBlh hopIncLocBlh;
	 
	 @Resource
	 private HopIncBlh hopIncBlh;
	 
	 @Resource
	 private HopCtlocBlh hopCtlocBlh;
	 
	 @Resource
	 private HopVendorBlh hopVendorBlh;
	 
	 @Resource
	 private  OrdBlh ordBlh;
	 
	 @Resource
	 private CommonService commonService;
	 
	/* (non-Javadoc)
	 * @see com.dhcc.scm.ws.his.HisInfoServiceInterface#getHopInc(com.dhcc.scm.entity.vo.ws.VenIncWeb)
	 */
	@Override
	public OperateResult getHopInc(HisIncWeb hisIncWeb) {
		OperateResult operateResult=new OperateResult();
		try {
			hopIncBlh.syncHisInc(operateResult,hisIncWeb);
		} catch (Exception e) {
			operateResult.setResultCode("1");
			operateResult.setResultContent("程序异常->Exception:"+e.getMessage());
			return operateResult;
		}
		return operateResult;
	}

	/**
	 * 
	 */
	@Override
	public OperateResult getHisOrder(HisOrderWebVo hisOrderWebVo) {
		OperateResult operateResult=new OperateResult();
		try {
			blh.importOrderByWS(hisOrderWebVo, operateResult);
		} catch (Exception e) {
			e.printStackTrace();
			operateResult.setResultCode("1");
			operateResult.setResultContent("程序异常1:"+e.getMessage());
		}
		return operateResult;
	}

	

	/* (non-Javadoc)
	 * @see com.dhcc.scm.ws.his.HisInfoServiceInterface#getRecItmByInv(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public HisInvInfoWeb getRecItmByInv(String invNo, String hopName,
			String venName) {
		HisInvInfoWeb hisInvInfoWeb=new HisInvInfoWeb();
		
		SysLog log=new SysLog();
		try {
			blh.getRecItmByInvWSSub(invNo, hopName,venName,hisInvInfoWeb);
			
			log.setOpArg(JsonUtils.toJson(hisInvInfoWeb)+",invNo:"+invNo+",venName:"+venName);
			log.setOpName("webservice医院入库查找发票");
			log.setOpDate(new Date());
			log.setOpResult(JsonUtils.toJson(hisInvInfoWeb.getResultContent()));
			log.setOpType("webservice");
			log.setOpUser(hopName);
		} catch (Exception e) {
			hisInvInfoWeb.setResultCode("1");
			hisInvInfoWeb.setResultContent("程序异常1:"+e.getMessage());
			log.setOpResult(e.getMessage());
		}finally{
			commonService.saveOrUpdate(log);
		}
		return hisInvInfoWeb;
	}

	/* (non-Javadoc)
	 * @see com.dhcc.scm.ws.his.HisInfoServiceInterface#cmpRec(java.util.List, java.lang.String)
	 */
	@Override
	public OperateResult cmpRec(HisCmpRecWeb hisCmpRecWeb) {
		// TODO Auto-generated method stub
		OperateResult operateResult=new OperateResult();
		try {
			operateResult.setResultCode("0");
			operateResult.setResultContent("success");
			blh.cmpRecWS(operateResult,hisCmpRecWeb);
		} catch (Exception e) {
			operateResult.setResultCode("1");
			operateResult.setResultContent("程序异常1:"+e.getMessage());
			return operateResult;
		}
		return operateResult;
	}

	@Override
	public OperateResult getHopLocIncQty(HisIncLocQtyWeb hisIncLocQtyWeb) {
		// TODO Auto-generated method stub
		OperateResult operateResult=new OperateResult();
		SysLog log=new SysLog();
		log.setOpArg(JsonUtils.toJson(hisIncLocQtyWeb));
		log.setOpName("webservice同步医院科室库存");
		log.setOpDate(new Date());
		log.setOpType("webservice");
		log.setOpUser(hisIncLocQtyWeb.getUserName());
		try {
			operateResult.setResultCode("0");
			operateResult.setResultContent("success");
			hopIncLocBlh.saveHisLocQty(operateResult, hisIncLocQtyWeb);
			log.setOpResult(JsonUtils.toJson(operateResult));
		} catch (Exception e) {
			operateResult.setResultCode("1");
			operateResult.setResultContent("程序异常1:"+e.getMessage());
			log.setOpResult(JsonUtils.toJson(operateResult));
			return operateResult;
		}finally{
			commonService.saveOrUpdate(log);
		}
		return operateResult;
	}

	@Override
	public OperateResult getHopLoc(HisLocWeb hisLocWeb) {
		OperateResult operateResult=new OperateResult();
		try {
			hopCtlocBlh.syncHisLoc(operateResult, hisLocWeb);
		} catch (Exception e) {
			operateResult.setResultCode("1");
			operateResult.setResultContent("程序异常->Exception:"+e.getMessage());
			return operateResult;
		}
		return operateResult;
	}

	@Override
	public OperateResult getHopVendor(HisVendorWeb hisVendorWeb) {
		OperateResult operateResult=new OperateResult();
		SysLog log = new SysLog();
		log.setOpArg(JsonUtils.toJson(hisVendorWeb));
		log.setOpName("webservice同步医院供应商信息");
		log.setOpDate(new Date());
		log.setOpType("webservice");
		log.setOpUser(hisVendorWeb.getUserName());
		try {
			
			hopVendorBlh.syncHisVendor(operateResult, hisVendorWeb);
		} catch (Exception e) {
			operateResult.setResultCode("1");
			operateResult.setResultContent("程序异常->Exception:"+e.getMessage());
			return operateResult;
		}finally{
			log.setOpResult(JsonUtils.toJson(operateResult));
			commonService.saveOrUpdate(log);
		}
		return operateResult;
	}

	@Override
	public OperateResult getHopOrder(HisOrderWeb hisOrderWeb) {
		OperateResult operateResult=new OperateResult();
		SysLog log = new SysLog();
		log.setOpArg(JsonUtils.toJson(hisOrderWeb));
		log.setOpName("webservice同步医院订单信息");
		log.setOpDate(new Date());
		log.setOpType("webservice");
		log.setOpUser(hisOrderWeb.getUserName());
		try {
			ordBlh.syncHisOrder(operateResult, hisOrderWeb);
		} catch (Exception e) {
			operateResult.setResultCode("1");
			operateResult.setResultContent("程序异常->Exception:"+e.getMessage());
			return operateResult;
		}finally{
			log.setOpResult(JsonUtils.toJson(operateResult));
			commonService.saveOrUpdate(log);
		}
		return operateResult;
	}

	@SuppressWarnings({ "unchecked"})
	@Override
	public HisInGdRec listInGdRec(String usename, String password) {
		
		HisInGdRec hisInGdRec=new HisInGdRec();
		SysLog log = new SysLog();
		log.setOpArg("usename:"+usename+",password:"+password);
		log.setOpName("webservice同步医院发货信息");
		log.setOpDate(new Date());
		log.setOpType("webservice");
		log.setOpUser(usename);
		try {
			DetachedCriteria detachedCriteria=DetachedCriteria.forClass(StInGdRecItm.class);
			detachedCriteria.add(Restrictions.isNull("ingdrecitmWsflag"));
			List<StInGdRecItm> stInGdRecItms=commonService.findByDetachedCriteria(detachedCriteria);
			for(StInGdRecItm stInGdRecItm:stInGdRecItms){
				HisInGdRecItm hisInGdRecItm=new HisInGdRecItm();
				hisInGdRecItm.setBatNo(stInGdRecItm.getIngdrecitmBatNo());
				hisInGdRecItm.setExpdate(stInGdRecItm.getIngdrecitmExpDate());
				hisInGdRecItm.setIncBarCode(stInGdRecItm.getIngdrecitmIncBarCode());
				hisInGdRecItm.setIngdrecId(stInGdRecItm.getIngdrecitmId());
				hisInGdRecItm.setInvNo(stInGdRecItm.getIngdrecitmInvNo());
				hisInGdRecItm.setQty(stInGdRecItm.getIngdrecitmQty());
				hisInGdRecItm.setRp(stInGdRecItm.getIngdrecitmRp());
				hisInGdRecItm.setHisNo(stInGdRecItm.getIngdrecitmHisNo());
				hisInGdRecItm.setPurLocCode(stInGdRecItm.getIngdrecitmPurLocCode());
				hisInGdRecItm.setRecLocCode(stInGdRecItm.getIngdrecitmRecLocCode());
				hisInGdRecItm.setVendorCode(stInGdRecItm.getIngdrecitmVendorCode());
				hisInGdRec.getInGdRecItms().add(hisInGdRecItm);
			}
			hisInGdRec.setResultCode("0");
			hisInGdRec.setResultContent("success");
		} catch (Exception e) {
			hisInGdRec.setResultCode("1");
			hisInGdRec.setResultContent("程序异常->Exception:"+e.getMessage());
			return hisInGdRec;
		}finally{
			log.setOpResult(JsonUtils.toJson(hisInGdRec));
			commonService.saveOrUpdate(log);
		}
		return hisInGdRec;
	}

	@Override
	public OperateResult cmpInGdRec(String usename, String password, Long ingdrecId) {
		
		OperateResult operateResult=new OperateResult();
		SysLog log = new SysLog();
		log.setOpArg("usename:"+usename+",password:"+password);
		log.setOpName("webservice同步医院入库信息：》cmpInGdRec");
		log.setOpDate(new Date());
		log.setOpType("webservice");
		log.setOpUser(usename);
		try {
			if(ingdrecId==null){
				operateResult.setResultContent("入参为空!");
				return operateResult;
			}
			StInGdRecItm stInGdRecItm=commonService.get(StInGdRecItm.class, ingdrecId);
			if(stInGdRecItm!=null){
				stInGdRecItm.setIngdrecitmWsflag("1");
				commonService.saveOrUpdate(stInGdRecItm);
				operateResult.setResultCode("0");
				operateResult.setResultContent("success");
			}
		} catch (Exception e) {
			operateResult.setResultCode("1");
			operateResult.setResultContent("程序异常->Exception:"+e.getMessage());
			return operateResult;
		}finally{
			log.setOpResult(JsonUtils.toJson(operateResult));
			commonService.saveOrUpdate(log);
		}
		return operateResult;
	}

	@Override
	public OperateResult getInvByRec(String usename, String password, Long ingdrecId) {
		
		OperateResult operateResult=new OperateResult();
		SysLog log = new SysLog();
		log.setOpArg("usename:"+usename+",password:"+password+",ingdrecId:"+ingdrecId);
		log.setOpName("webservice获取发票号：》getInvByRec");
		log.setOpDate(new Date());
		log.setOpType("webservice");
		log.setOpUser(usename);
		try {
			if(ingdrecId==null){
				operateResult.setResultContent("入参为空!");
				return operateResult;
			}
			StInGdRecItm stInGdRecItm=commonService.get(StInGdRecItm.class, ingdrecId);
			if(stInGdRecItm!=null){
				operateResult.setResultCode("0");
				operateResult.setResultContent(stInGdRecItm.getIngdrecitmInvNo());
			}
		} catch (Exception e) {
			operateResult.setResultCode("1");
			operateResult.setResultContent("程序异常->Exception:"+e.getMessage());
			return operateResult;
		}finally{
			log.setOpResult(JsonUtils.toJson(operateResult));
			commonService.saveOrUpdate(log);
		}
		return operateResult;
	}

	@Override
	public OperateResult syncInvByRec(String usename, String password, Long ingdrecId, String invno) {
		OperateResult operateResult=new OperateResult();
		SysLog log = new SysLog();
		log.setOpArg("usename:"+usename+",password:"+password+",ingdrecId:"+ingdrecId+",invno:"+invno);
		log.setOpName("webservice同步医院发票号：》syncInvByRec");
		log.setOpDate(new Date());
		log.setOpType("webservice");
		log.setOpUser(usename);
		try {
			if(ingdrecId==null){
				operateResult.setResultContent("入库表id为空!");
				return operateResult;
			}
			if(StringUtils.isBlank(invno)){
				operateResult.setResultContent("发票号为空!");
				return operateResult;
			}
			StInGdRecItm stInGdRecItm=commonService.get(StInGdRecItm.class, ingdrecId);
			if(stInGdRecItm!=null){
				stInGdRecItm.setIngdrecitmInvNo(invno);
				OrderDetailSub detailSub=commonService.get(OrderDetailSub.class, stInGdRecItm.getIngdrecitmOrdsubId());
				commonService.saveOrUpdate(detailSub);
				commonService.saveOrUpdate(stInGdRecItm);
				operateResult.setResultCode("0");
			}
		} catch (Exception e) {
			operateResult.setResultCode("1");
			operateResult.setResultContent("程序异常->Exception:"+e.getMessage());
			return operateResult;
		}finally{
			log.setOpResult(JsonUtils.toJson(operateResult));
			commonService.saveOrUpdate(log);
		}
		return operateResult;
	}

	@Override
	public FileWrapper downLoadPic(String type, String name) {
		// TODO Auto-generated method stub
		
		FileWrapper fileWrapper=new FileWrapper();
		SysLog log = new SysLog();
		log.setOpArg("type:"+type+",path:"+name);
		log.setOpName("webservice下载图片：》downLoadPic");
		log.setOpDate(new Date());
		log.setOpType("webservice");
		if(StringUtils.isBlank(name)){
			fileWrapper.setResultContent("path不能为空");
			log.setOpResult("path不能为空");
			return fileWrapper;
		}
		try {
		
			String result=this.getClass().getClassLoader().getResource("").getPath();
			int index = result.indexOf("WEB-INF"); 
			if(index == -1){ 
				index = result.indexOf("bin"); 
			} 
			result = result.substring(0,index); 
			fileWrapper.setFileName(name);
			fileWrapper.setFileExtension(com.dhcc.framework.util.FileUtils.getFileExp(name));
			log.setOpAfter(result+"uploads/weixin/"+name);
			String path="uploads/weixin/";
			switch (type) {
			case "ORDER":
				path="uploads/weixin/order/";
				break;

			default:
				break;
			}
			
			DataSource source = new FileDataSource(new File(result+path+name));
			fileWrapper.setFile(new DataHandler(source));
			fileWrapper.setResultCode("0");
		} catch (Exception e) {
			e.printStackTrace();
			log.setOpResult(e.getMessage());
			fileWrapper.setResultCode("-11");
			fileWrapper.setResultContent(e.getMessage());
			return fileWrapper;
		}finally{
			commonService.saveOrUpdate(log);
		}
		return fileWrapper;
	}

	@Override
	public HisInGdRec getOrderDetail(String orderno) {
		HisInGdRec hisInGdRec=new HisInGdRec();
		List<HisInGdRecItm> inGdRecItms =new ArrayList<HisInGdRecItm>();
		List<HisPicWeb> hisPicWebs=new ArrayList<HisPicWeb>();
		try {
			if(orderno==null){
				hisInGdRec.setResultContent("入库表id为空!");
				return hisInGdRec;
			}
			List<OrderDetail> OrderDetails=commonService.findByProperty(OrderDetail.class, "orderNo", orderno);
			if(OrderDetails!=null){
				for(OrderDetail orderdetil:OrderDetails)
				{
					HopInc hopInc=commonService.get(HopInc.class, orderdetil.getOrderHopIncId());
					String purLoc="",recLoc="";
					if(orderdetil.getOrderPurLoc()!=null){
						purLoc=commonService.get(HopCtloc.class, orderdetil.getOrderPurLoc()).getCode();
					}
					if(orderdetil.getOrderRecLoc()!=null){
						recLoc=commonService.get(HopCtloc.class, orderdetil.getOrderRecLoc()).getCode();
					}
					Vendor vendor=commonService.get(Vendor.class, orderdetil.getOrderVenId());
					List<OrderDetailSub> detailSubs=commonService.findByProperty(OrderDetailSub.class, "ordSubDetailId",orderdetil.getOrderId());
					for(OrderDetailSub detailsub:detailSubs)
					{
						HisInGdRecItm hisInGdRecItm=new HisInGdRecItm();
						hisInGdRecItm.setBatNo(detailsub.getOrdSubBatNo());
						hisInGdRecItm.setExpdate(detailsub.getOrdSubExpDate());
						hisInGdRecItm.setArriveDate(detailsub.getOrdSubArriveDate());
						hisInGdRecItm.setHisNo(orderdetil.getOrderHisNo());
						hisInGdRecItm.setIncBarCode(hopInc.getIncBarCode());
						hisInGdRecItm.setInvNo(detailsub.getOrdSubInvNo());
						hisInGdRecItm.setOrderno(orderdetil.getOrderNo());
						hisInGdRecItm.setPurLocCode(purLoc);
						hisInGdRecItm.setQty(detailsub.getOrderSubQty());
						hisInGdRecItm.setRecLocCode(recLoc);
						hisInGdRecItm.setRp(detailsub.getOrderSubRp());
						hisInGdRecItm.setVendorCode(vendor.getCode());
						hisInGdRecItm.setOrderDetailSubId(detailsub.getOrdSubId());
						inGdRecItms.add(hisInGdRecItm);
					}
					
				}
			}
			List<OrderDetailPic> orderDetailPics=commonService.findByProperty(OrderDetailPic.class, "ordPicOrderNo", orderno);
			for(OrderDetailPic detailPic:orderDetailPics)
			{
				hisPicWebs.add(new HisPicWeb("ORDER", detailPic.getOrdPicPath()));
			}
			hisInGdRec.setInGdRecItms(inGdRecItms);
			hisInGdRec.setPicWebs(hisPicWebs);
		} catch (Exception e) {
			hisInGdRec.setResultCode("1");
			hisInGdRec.setResultContent("程序异常->Exception:"+e.getMessage());
			return hisInGdRec;
		}
		return hisInGdRec;
	}

	@Override
	public OperateResult cmpOrder(String username, String password, List<String> ordsubs) {
		
		OperateResult operateResult=new OperateResult();
		SysLog log = new SysLog();
		log.setOpArg("usename:"+username+",password:"+password+",ordsubs:"+JsonUtils.toJson(ordsubs));
		log.setOpName("webservice库房扫码确认完成：》cmpOrder");
		log.setOpDate(new Date());
		log.setOpType("webservice");
		log.setOpUser(username);
		try {
			NormalAccount normalAccount=ordBlh.checkWsParam(operateResult, username, password, ordsubs);
			if(normalAccount==null){
				return operateResult;
			}
			for(String sub:ordsubs){
				OrderDetailSub orderDetailSub=commonService.get(OrderDetailSub.class, sub);
				if(orderDetailSub!=null){
					if(orderDetailSub.getOrdSubStatus().equals("Y")){
						orderDetailSub.setOrdSubStatus("T");
						commonService.saveOrdSub(orderDetailSub, "库房扫码入库确认", normalAccount.getAccountId());
					}
				}
			}
			operateResult.setResultCode("0");
			operateResult.setResultContent("success");
		} catch (Exception e) {
			operateResult.setResultCode("1");
			operateResult.setResultContent("程序异常->Exception:"+e.getMessage());
			return operateResult;
		}finally{
			log.setOpResult(JsonUtils.toJson(operateResult));
			commonService.saveOrUpdate(log);
		}
		return operateResult;
	}

    
    
}
