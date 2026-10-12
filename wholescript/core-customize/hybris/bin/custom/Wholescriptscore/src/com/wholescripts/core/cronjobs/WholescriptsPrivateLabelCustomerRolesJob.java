package com.wholescripts.core.cronjobs;

import com.wholescripts.core.dao.WholescriptsUserDao;
import com.wholescripts.core.enums.CustomerProfileType;
import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.*;

public class WholescriptsPrivateLabelCustomerRolesJob extends AbstractJobPerformable<CronJobModel> {

    private static final Logger LOG = LoggerFactory.getLogger(WholescriptsPrivateLabelCustomerRolesJob.class);

    private static final String PRIVATE_LABEL_CUSTOMER_GROUP = "privatelabelcustomergroup";

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "modelService")
    private ModelService modelService;

    @Resource(name = "wholescriptsUserDao")
    private WholescriptsUserDao wholescriptsUserDao;

    @Override
    public PerformResult perform(CronJobModel cronJobModel) {
        long startTime = System.currentTimeMillis();
        try {
            List<CustomerMaterialInformationModel> customerMaterialAccounts = wholescriptsUserDao.getDistinctAccountNumberFromCustomerMaterial();
            List<B2BUnitModel> b2bUnitAccounts = wholescriptsUserDao.getAllB2bUnits();

            LOG.info("Fetched {} customer material accounts.", customerMaterialAccounts.size());
            LOG.info("Fetched {} B2B units.", b2bUnitAccounts.size());

            Map<String, B2BUnitModel> accountToUnitMap = new HashMap<>();
            for (B2BUnitModel unit : b2bUnitAccounts) {
                String cleanedAccount = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(unit.getUid());
                if (StringUtils.isNotEmpty(cleanedAccount)) {
                    accountToUnitMap.put(cleanedAccount, unit);
                }
            }

            Set<B2BCustomerModel> customersToUpdate = new HashSet<>();

            for (CustomerMaterialInformationModel customerAccount : customerMaterialAccounts) {
                String formattedCustomerAccount = String.format("%010d", Integer.valueOf(customerAccount.getAccount()));

                B2BUnitModel matchedUnit = accountToUnitMap.get(formattedCustomerAccount);

                if (matchedUnit != null) {
                    customersToUpdate.addAll(findEligibleCustomers(matchedUnit));
                }
            }

            if (!customersToUpdate.isEmpty()) {
                modelService.saveAll(customersToUpdate);
                LOG.info("Saved {} customers with updated groups.", customersToUpdate.size());
            } else {
                LOG.info("No customers needed updating.");
            }

            long endTime = System.currentTimeMillis();
            LOG.info("wholescriptsPrivateLabelCustomerRolesJob completed successfully in {} ms", (endTime - startTime));
            return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
        } catch (Exception e) {
            LOG.error("Error occurred during wholescriptsPrivateLabelCustomerRolesJob execution", e);
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
        }
    }

    private List<B2BCustomerModel> findEligibleCustomers(B2BUnitModel b2bUnit) {
        List<B2BCustomerModel> eligibleCustomers = new ArrayList<>();
        Set<PrincipalModel> members = b2bUnit.getMembers();
        LOG.debug("Processing B2BUnit: {} with {} members", b2bUnit.getUid(), members.size());

        UserGroupModel privateLabelGroup = getPrivateLabelGroup();

        for (PrincipalModel member : members) {
            if (member instanceof B2BCustomerModel) {
                B2BCustomerModel customer = (B2BCustomerModel) member;
                if (isPractitionerCustomer(customer)) {
                    Set<PrincipalGroupModel> groups = new HashSet<>(customer.getGroups());
                    if (!groups.contains(privateLabelGroup)) {
                        groups.add(privateLabelGroup);
                        customer.setGroups(groups);
                        eligibleCustomers.add(customer);
                        LOG.debug("Customer {} updated with private label group", customer.getUid());
                    } else {
                        LOG.debug("Customer {} already has private label group, skipping", customer.getUid());
                    }
                }
            }
        }
        return eligibleCustomers;
    }

    private boolean isPractitionerCustomer(B2BCustomerModel customer) {
        return CustomerProfileType.PRACTITIONER.equals(customer.getCustomerProfile());
    }

    private UserGroupModel getPrivateLabelGroup() {
        return userService.getUserGroupForUID(PRIVATE_LABEL_CUSTOMER_GROUP);
    }
}
