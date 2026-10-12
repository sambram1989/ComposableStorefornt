export const environment = {
  stage: {
    occ: 'https://api.c1lud0zblk-xymogenin1-s1-public.model-t.cc.commerce.ondemand.com',
    cdcKey: '4_Q2Samg1FazA8ipcXZEEcCg',
    WHOLESCRIPTS_STORE: 'wholescripts',
    analyticsEnable: true,
    persona: {
      templateId:{
        patient: 'itmpl_vx1J1aKFMeNU227gp4JSLaxtMhT2',
        practitioner: 'itmpl_6DtY1qhtyrSaGMSmVimL2Z9fNUYn'
      },
      referenceId: '10436227',
      environmentId: 'env_SB4cbvDZEdLVDUqXkhLf8fRHLrmB'
    },
    smartEditOrigin: '*.c1lud0zblk-xymogenin1-s1-public.model-t.cc.commerce.ondemand.com',
    accertify: {
      url: 'https://test.accdab.net/cdn/cs/QAeDTHJ2NxJKEBA1Sxmn3SzXItg.js',
    },
    klaviyo: 'X4ifGT',
    achPayment: {
      url: 'https://xymogen-xeq-public.delego-cloud.com/DelegoRP/'
    }
  },
  dev: {
    occ: 'https://api.c1lud0zblk-xymogenin1-d1-public.model-t.cc.commerce.ondemand.com',
    cdcKey: '4_vxisPjIAFMKPEphelJZ9cw',
    analyticsEnable: true,
    WHOLESCRIPTS_STORE: 'wholescripts',
    persona: {
      templateId:{
        patient: 'itmpl_vx1J1aKFMeNU227gp4JSLaxtMhT2',
        practitioner: 'itmpl_6DtY1qhtyrSaGMSmVimL2Z9fNUYn'
      },
      referenceId: '10436227',
      environmentId: 'env_SB4cbvDZEdLVDUqXkhLf8fRHLrmB'
    },
    smartEditOrigin: '*.c1lud0zblk-xymogenin1-d1-public.model-t.cc.commerce.ondemand.com',
    accertify: {
      url: 'https://test.accdab.net/cdn/cs/QAeDTHJ2NxJKEBA1Sxmn3SzXItg.js',
    },
    klaviyo: 'X4ifGT',
    achPayment: {
      url: 'https://xymogen-xeq-public.delego-cloud.com/DelegoRP/'
    }
  },
};

export const selectedEnv = environment.dev;

//user Roles mapping
export const userMap: any = {
    patientdirectcustomergroup:'patientdirect',
    privatelabelcustomergroup: 'pluser',
    wholescriptdoctorgroup: 'doctor',
    practitionercustomergroup: 'practitioner',
    patientcustomergroup: 'patient',
    employeecustomergroup:'employee',
    friendsfamilycustomergroup: 'friendsfamily',
    salesrepcustomergroup: 'salesrep',
    distributorcustomergroup: 'distributorcustomer',
    studentcustomergroup: 'studentcustomer'
};
