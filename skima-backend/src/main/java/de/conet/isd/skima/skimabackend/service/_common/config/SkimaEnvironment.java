package de.conet.isd.skima.skimabackend.service._common.config;

/**
 * The operating environment that this instance is running on
 */
public enum SkimaEnvironment {
    /*
     * Note that this currently corralates 1-to-1 to spring profiles, but that's not necessarily a must for the future!
     */

    LOCAL, QA_CLOUD, QA_ENTERPRISE, PROD_CLOUD, PROD_ENTERPRISE;
}
