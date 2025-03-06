package com.jwzt.DB.cdn.region;

public class RegionInfo implements java.io.Serializable
{
	private static final long serialVersionUID = -1446859074967792661L;

	/**
	 * 区域id
	 */
	private Integer server_region_id;

	/**
	 * 区域名称
	 */
	private String server_region_name;

	/**
	 * 父亲区域id
	 */
	private Integer parent_region_id;

	/**
	 * 是否允许其他区域用户访问 0：允许 1：不允许
	 */
	private Integer if_visit;

	/**
	 * 根区域id
	 */
	private Integer root_region_id;

	/**
	 * ip段的id
	 */
	private String ip_address_id;

	/**
	 * 区域所处的层
	 */
	private Integer layer = 0;

	/**
	 * 图片路径
	 */
	private String tree_images;

	/**
	 * 栏目属性
	 */
	private String node_attr;

	/**
	 * 区域描述
	 */
	private String region_desc;


	/** default constructor */
	public RegionInfo()
	{
		this.node_attr = "4";
	}

	/** minimal constructor */
	public RegionInfo(Integer server_region_id, String server_region_name)
	{
		this.server_region_id = server_region_id;
		this.server_region_name = server_region_name;
	}

	/** full constructor */
	public RegionInfo(Integer server_region_id, String server_region_name, Integer parent_region_id, Integer if_visit,
			Integer root_region_id, String ip_address_id, Integer layer, String tree_images, String node_attr, String region_desc)
	{
		this.server_region_id = server_region_id;
		this.server_region_name = server_region_name;
		this.parent_region_id = parent_region_id;
		this.if_visit = if_visit;
		this.root_region_id = root_region_id;
		this.ip_address_id = ip_address_id;
		this.layer = layer;
		this.tree_images = tree_images;
		this.node_attr = node_attr;
		this.region_desc = region_desc;
	}

	// Property accessors
	/**
	 * 区域id
	 */

	public Integer getServer_region_id()
	{
		return this.server_region_id;
	}

	public void setServer_region_id(Integer server_region_id)
	{
		this.server_region_id = server_region_id;
	}

	/**
	 * 区域名称
	 */

	public String getServer_region_name()
	{
		return this.server_region_name;
	}

	public void setServer_region_name(String server_region_name)
	{
		this.server_region_name = server_region_name;
	}

	/**
	 * 父亲区域id
	 */

	public Integer getParent_region_id()
	{
		return this.parent_region_id;
	}

	public void setParent_region_id(Integer parent_region_id)
	{
		this.parent_region_id = parent_region_id;
	}

	/**
	 * 是否允许其他区域用户访问 0：允许 1：不允许
	 */

	public Integer getIf_visit()
	{
		return this.if_visit;
	}

	public void setIf_visit(Integer if_visit)
	{
		this.if_visit = if_visit;
	}

	/**
	 * 根区域id
	 */

	public Integer getRoot_region_id()
	{
		return this.root_region_id;
	}

	public void setRoot_region_id(Integer root_region_id)
	{
		this.root_region_id = root_region_id;
	}

	/**
	 * ip段的id
	 */

	public String getIp_address_id()
	{
		return this.ip_address_id;
	}

	public void setIp_address_id(String ip_address_id)
	{
		this.ip_address_id = ip_address_id;
	}

	/**
	 * 区域所处的层
	 */

	public Integer getLayer()
	{
		return this.layer;
	}

	public void setLayer(Integer layer)
	{
		this.layer = layer;
	}

	/**
	 * 图片路径
	 */

	public String getTree_images()
	{
		return this.tree_images;
	}

	public void setTree_images(String tree_images)
	{
		this.tree_images = tree_images;
	}

	/**
	 * 栏目属性
	 */

	public String getNode_attr()
	{
		return this.node_attr;
	}

	public void setNode_attr(String node_attr)
	{
		this.node_attr = node_attr;
	}

	/**
	 * 区域描述
	 */

	public String getRegion_desc()
	{
		return this.region_desc;
	}

	public void setRegion_desc(String region_desc)
	{
		this.region_desc = region_desc;
	}

}
