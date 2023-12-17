/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author OMAR
 */
public class ContributorInfoDTO {

        private final String contributorName;
        private final int contributionAmount;

    public ContributorInfoDTO(String contributorName, int contributionAmount) {
        this.contributorName = contributorName;
        this.contributionAmount = contributionAmount;
    }

    public String getContributorName() {
        return contributorName;
    }

    public int getContributionAmount() {
        return contributionAmount;
    }
}
