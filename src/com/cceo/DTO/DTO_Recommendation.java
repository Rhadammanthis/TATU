package com.cceo.DTO;

public class DTO_Recommendation {

	private String idDieta;
	private String recommendationMoreFirst;
	private String recommendationMoreSecond;
	private String recommendationSameFirst;
	private String recommendationSameSecond;
	private String recommendationLessFirst;
	private String recommendationLessSecond;
	
	public DTO_Recommendation(String idDieta, String recommendationMoreFirst,
			String recommendationMoreSecond, String recommendationSameFirst,
			String recommendationSameSecond, String recommendationLessFirst,
			String recommendationLessSecond) {
		super();
		this.idDieta = idDieta;
		this.recommendationMoreFirst = recommendationMoreFirst;
		this.recommendationMoreSecond = recommendationMoreSecond;
		this.recommendationSameFirst = recommendationSameFirst;
		this.recommendationSameSecond = recommendationSameSecond;
		this.recommendationLessFirst = recommendationLessFirst;
		this.recommendationLessSecond = recommendationLessSecond;
	}
	
	public String getIdDieta() {
		return idDieta;
	}
	public void setIdDieta(String idDieta) {
		this.idDieta = idDieta;
	}
	public String getRecommendationMoreFirst() {
		return recommendationMoreFirst;
	}
	public void setRecommendationMoreFirst(String recommendationMoreFirst) {
		this.recommendationMoreFirst = recommendationMoreFirst;
	}
	public String getRecommendationMoreSecond() {
		return recommendationMoreSecond;
	}
	public void setRecommendationMoreSecond(String recommendationMoreSecond) {
		this.recommendationMoreSecond = recommendationMoreSecond;
	}
	public String getRecommendationSameFirst() {
		return recommendationSameFirst;
	}
	public void setRecommendationSameFirst(String recommendationSameFirst) {
		this.recommendationSameFirst = recommendationSameFirst;
	}
	public String getRecommendationSameSecond() {
		return recommendationSameSecond;
	}
	public void setRecommendationSameSecond(String recommendationSameSecond) {
		this.recommendationSameSecond = recommendationSameSecond;
	}
	public String getRecommendationLessFirst() {
		return recommendationLessFirst;
	}
	public void setRecommendationLessFirst(String recommendationLessFirst) {
		this.recommendationLessFirst = recommendationLessFirst;
	}
	public String getRecommendationLessSecond() {
		return recommendationLessSecond;
	}
	public void setRecommendationLessSecond(String recommendationLessSecond) {
		this.recommendationLessSecond = recommendationLessSecond;
	}
}
