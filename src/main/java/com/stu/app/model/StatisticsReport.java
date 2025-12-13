package com.stu.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Statistics_report")
public class StatisticsReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "avg_score")
    private Double avgScore; // Điểm trung bình

    @Column(name = "rank_distribution", columnDefinition = "NVARCHAR(MAX)")
    private String rankDistribution; // Phân bố xếp loại (JSON hoặc Text mô tả)

    public StatisticsReport() {}

    public Long getReportId() { return reportId; }
    public void setReportId(Long reportId) { this.reportId = reportId; }
    public Double getAvgScore() { return avgScore; }
    public void setAvgScore(Double avgScore) { this.avgScore = avgScore; }
    public String getRankDistribution() { return rankDistribution; }
    public void setRankDistribution(String rankDistribution) { this.rankDistribution = rankDistribution; }
}