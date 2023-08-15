package zhishusz.housepresell.database.po.state;

public enum S_AcceptFileType {

	Picture("图片", ".BMP,.PCX,.PNG,.JPEG,.GIF,.TIFF,.DXF,.CGM,.CDR,.WMF,.EPS,.EMF,.PICT,.VSD,.JPG,png,jpg,jpeg"), 
	ZIpFile("压缩文件", ".rar,.zip,.cab,.iso,.jar,.ace,.7z,.tar,.gz,.arj,.lzh,.uue,.bz2,.z"), 
	Word("Word", ".wps,.doc,.docx,.ppt,.txt"), 
	Excel("Excel", ".xls,.xlsx,.xlsb,.CSV"), 
	PDF("PDF","pdf"),
	Other("其他", "其他");

	private String Name;
	private String Type;
	
	S_AcceptFileType(String Name, String Type) {
		this.Name = Name;
		this.Type = Type;
	}
	
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}
	
	
}
