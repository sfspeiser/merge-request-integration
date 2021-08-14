package net.ntworld.mergeRequestIntegrationIde.task

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator
import net.ntworld.mergeRequest.Commit
import net.ntworld.mergeRequest.MergeRequest
import net.ntworld.mergeRequest.ProviderData
import net.ntworld.mergeRequest.query.GetCommitsQuery
import net.ntworld.mergeRequestIntegration.make
import net.ntworld.mergeRequestIntegration.provider.gitlab.request.GitlabFindMRRequest
import net.ntworld.mergeRequestIntegration.provider.gitlab.transformer.GitlabMRTransformer
import net.ntworld.mergeRequestIntegrationIde.infrastructure.ProjectServiceProvider

class GetCommitsTask(
    private val projectServiceProvider: ProjectServiceProvider,
    private val providerData: ProviderData,
    private val mergeRequest: MergeRequest,
    private val listener: Listener
) : Task.Backgroundable(projectServiceProvider.project, "Fetching commit data...", true) {
    fun start() {
        ProgressManager.getInstance().runProcessWithProgressAsynchronously(
            this,
            Indicator(this)
        )
    }

    private class Indicator(private val task: GetCommitsTask) : BackgroundableProcessIndicator(task)

    override fun run(indicator: ProgressIndicator) {
        try {
            listener.taskStarted()
            val result = projectServiceProvider.infrastructure.queryBus() process GetCommitsQuery.make(
                providerId = providerData.id,
                mergeRequestId = mergeRequest.id
            )
            listener.dataReceived(mergeRequest, result.commits)
            projectServiceProvider.reviewContextManager.updateCommits(
                providerData.id, mergeRequest.id, result.commits
            )
            listener.taskEnded()
        } catch (exception: Exception) {
            listener.onError(exception)
        }
    }

    interface Listener {
        fun onError(exception: Exception) {
            throw exception
        }

        fun taskStarted() {}

        fun dataReceived(mergeRequest: MergeRequest, commits: List<Commit>)

        fun taskEnded() {}
    }
}