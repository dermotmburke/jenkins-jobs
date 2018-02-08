import template.Template

Template.createJob(pipelineJob('coa-cwa-master'), [repoName: 'coa/coa-cwa', fileName: 'Jenkinsfile-master', includedBranches: 'master', excludedBranches: ''])
