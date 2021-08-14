package net.ntworld.mergeRequestIntegrationIde.infrastructure.service.repositoryFile

import com.intellij.openapi.vcs.changes.Change
import net.ntworld.mergeRequest.MergeRequestInfo
import net.ntworld.mergeRequest.ProviderData
import net.ntworld.mergeRequestIntegrationIde.infrastructure.service.RepositoryFileService
import javax.swing.Icon

open class RepositoryFileDecorator(
    private val service: RepositoryFileService
) : RepositoryFileService {

    override fun findChanges(providerData: ProviderData, mergeRequestInfo: MergeRequestInfo, hashes: List<String>): List<Change> {
        return service.findChanges(providerData, mergeRequestInfo, hashes)
    }

    override fun findIcon(providerData: ProviderData, path: String): Icon {
        return service.findIcon(providerData, path)
    }

}