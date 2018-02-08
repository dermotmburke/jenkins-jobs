package templates

class Template {

    static credentialsGitLab = 'gitlab'

    static defaultBranchName = 'master'
    static defaultFileName = 'Jenkinsfile'
    static defaultIncludedBranches = '*'
    static defaultExcludedBranches = '*'
    static hostNameGitLab = 'ssh://git@gitlab.servicing.sbx.zone:29418'

    static void createJob(job, Map params = [:]) {
        def config = createConfig(hostNameGitLab, job, params)

        addDef(job, config, credentialsGitLab)
    }

    private static void addDef(job, config, creds) {
        job.with {
            definition {
                cpsScm {
                    scm {
                        git {
                            remote {
                                url("${config.hostName}/${config.repoName}.git")
                                credentials(creds)
                                name('origin')
                                refspec('+refs/heads/*:refs/remotes/origin/* +refs/merge-requests/*/head:refs/remotes/origin/merge-requests/*')
                            }
                            branch('refs/heads/${gitlabSourceBranch}')
                        }
                    }
                    triggers {
                        gitlabPush {
                            buildOnMergeRequestEvents(true)
                            includeBranches("${config.includedBranches}")
                            excludeBranches("${config.excludedBranches}")
                        }
                    }
                    scriptPath("${config.fileName}")
                }
            }
        }
    }

    private static def createConfig(hostName, job, params) {
        // Initialise configuration with defaults
        def config = [hostName: hostName, repoName: job.name, branchName: defaultBranchName,
                      fileName: defaultFileName, includedBranches: defaultIncludedBranches,
                      excludedBranches: defaultExcludedBranches]

        // "Override" default configured variables
        config.putAll(params)

        return config
    }
}
