package com.sewage.springboot.entity.constant;

public class JobConstant {
    /* 工单当前处理进程状态 
     *  <br><br>
     * 状态码：     操作 --> 				产生的状态名（状态解释）<br>
     * 
     *  1:  创建 --> 				待受理<br> （任意用户创建工单）
     *  2:  分配/领取 --> 			处理中<br> （客服领取工单）
     *  3:  转发 --> 				进程1：转发中<br> （转发产生一个转发的进程，记录转发人。然后又产生一个处理中的进程，记录第二个处理人，相当于进入到了状态2）
     *      转发 --> 				进程2：处理中<br>  
     *  4:  客服处理完成 --> 		待检测人员确认问题已解决<br> （客服处理后，提交处理证明，点击完成。然后等待控制中心确认结果）
     *  5：     检测人员确认问题已解决 --> 已完成 （控制中心确认结果处理OK）
     *  6：     检测人员确认问题未解决 --> 已驳回|处理失败 （控制中心确认问题未解决）
     */
	
	/** 状态：创建/未受理  */
	public static final String JOB_STATUS_CREATE 		= "1";
	/** 状态：受理中 */
	public static final String JOB_STATUS_PROCESSING 	= "2";
	/** 状态：任务被转发  */
	public static final String JOB_STATUS_TRANSFER 		= "3";
	/** 状态：处理结束（待审核确认）  */
	public static final String JOB_STATUS_PROCESSED 	= "4";
	/** 状态：处理结果被检测中心确认处理完成  */
	public static final String JOB_STATUS_SUCCESS 		= "5";
	/** 状态：处理结果被检测中心确认未被处理成功，中断处理  */  //2020-1-15日修改
	public static final String JOB_STATUS_FAIL 			= "6";
	
	/** 状态：处理结果被检测中心确认未被处理成功，并驳回给处理者    */  //2020-1-15日新增 
	public static final String JOB_STATUS_REJECT		= "7";
}
