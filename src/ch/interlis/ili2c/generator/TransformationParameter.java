package ch.interlis.ili2c.generator;

public class TransformationParameter {
	// -o2 --out neuesModel.ili --trafoDiff diffx,diffy --trafoFactor factorx,factory --trafoEpsg epsgCode --trafoImports neusBasisModel=altesBasisModel --trafoNewModel neuesModel altesModel.ili
	private double factor_x=1.0;
	private double factor_y=1.0;
	private double diff_x=0.0;
	private double diff_y=0.0;
	private int epsgCode;
	private String newModelName;
	private ModelTransformation importModels[];
	public static class ModelTransformation {
		public String getFromModel() {
			return fromModel;
		}
		public String getToModel() {
			return toModel;
		}
		public ModelTransformation(String toModel, String fromModel) {
			this.toModel = toModel;
			this.fromModel = fromModel;
		}
		private String fromModel;
		private String toModel;
	}
	public double getFactor_x() {
		return factor_x;
	}
	public void setFactor_x(double factor_x) {
		this.factor_x = factor_x;
	}
	public double getFactor_y() {
		return factor_y;
	}
	public void setFactor_y(double factor_y) {
		this.factor_y = factor_y;
	}
	public double getDiff_x() {
		return diff_x;
	}
	public void setDiff_x(double diff_x) {
		this.diff_x = diff_x;
	}
	public double getDiff_y() {
		return diff_y;
	}
	public void setDiff_y(double diff_y) {
		this.diff_y = diff_y;
	}
	public int getEpsgCode() {
		return epsgCode;
	}
	public void setEpsgCode(int epsgCode) {
		this.epsgCode = epsgCode;
	}
	public ModelTransformation[] getImportModels() {
		return importModels;
	}
	public void setImportModels(ModelTransformation importModels[]) {
		this.importModels = importModels;
	}
	public String getNewModelName() {
		return newModelName;
	}
	public void setNewModelName(String newModelName) {
		this.newModelName = newModelName;
	}
}
