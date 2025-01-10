package de.conet.isd.skima.skimabackend.service._common.config;

public interface SpringPofiles {
    String LOCAL = "local";

    // qa/prod and cloud/enterpise are orthogonal concepts,
    // i.e. we have qa-cloud, qa-enterprise and prod-cloud, prod-enterprise combos

    String QA = "qa";
    String PROD = "prod";
    String CLOUD = "cloud";
    String ENTERPRISE = "enterprise";
}
