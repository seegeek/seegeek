
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONObject;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 10, 2015 9:15:37 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public class MessageJson {
	
	/**
	 * 状态标志
	 * 1: 真  0:假
	 * 1: alert   2: confirm
	 */
	private String flag ;
	
	/**
	 * 消息
	 */
	private String msg ;
	
	/**
	 * 虚拟机的状态代码
	 */
	private String vmStatus;
	
	/**
	 * 虚拟机状态描述。ajax显示===>多语言支持。
	 */
	private String vmStatusMsg;
	
	/**
	 * 获取的任务id
	 */
	private String taskId;
	
	/**
	 * 在VMAction.java中queryStatus使用。
	 */
	private String vmRunningTime;
	
	/**
	 * jianghs添加，2012009-06
	 */
	private String vmPcid;
	/**
	 * 数据集
	 */
	@SuppressWarnings("unchecked")
	private List items;
	
	/**
	 * 返回一个错误的messagejson事调用，虚拟机操作service方法调用
	 * @param errorMsg
	 * @return
	 */
	public static MessageJson getDefaultDaoErrorMessageJson(String errorMsg)
	{
		MessageJson mj = new MessageJson("0");//0是失败，1是成功。
		mj.setMsg(errorMsg);
		
		return mj;
	}
	
	public MessageJson(String flag){
		this.flag = flag;
	}
	public MessageJson(String flag,String msg){
		this.flag = flag;
		this.msg = msg;
	}
	@SuppressWarnings("unchecked")
	public MessageJson(String flag,String msg,List items){
		this.flag = flag;
		this.msg = msg;
		this.items = items;
	}
	
	
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@SuppressWarnings("unchecked")
	public List getItems() {
		if(null == items)
		{
			items = new ArrayList();
		}
		return items;
	}
	@SuppressWarnings("unchecked")
	public void setItems(List items) {
		this.items = items;
	}
	
	
	
	@Override
	public String toString() {
		
		JSONObject jo = JSONObject.fromObject(this);
		
		return jo.toString();
	}
	public String getVmStatus() {
		if(null == vmStatusMsg|| "".equals(vmStatusMsg.trim()))
		{
			vmStatusMsg = "";
		}
		return vmStatus;
	}
	public void setVmStatus(String vmStatus) {
		this.vmStatus = vmStatus;
	}
	public String getVmStatusMsg() {
		return vmStatusMsg;
	}
	public void setVmStatusMsg(String vmStatusMsg) {
		this.vmStatusMsg = vmStatusMsg;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getVmRunningTime() {
		return vmRunningTime;
	}
	public void setVmRunningTime(String vmRunningTime) {
		this.vmRunningTime = vmRunningTime;
	}

	public String getVmPcid() {
		return vmPcid;
	}

	public void setVmPcid(String vmPcid) {
		this.vmPcid = vmPcid;
	}
	public static void main(String args[])
	{
		
		
	}
}
