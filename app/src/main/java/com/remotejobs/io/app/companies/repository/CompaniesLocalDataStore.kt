package com.remotejobs.io.app.companies.repository

import com.remotejobs.io.app.interfaces.CompaniesLocalRepository
import com.remotejobs.io.app.data.database.dao.CompaniesDao
import com.remotejobs.io.app.data.database.dao.JobsDao
import com.remotejobs.io.app.model.Company
import com.remotejobs.io.app.model.Job
import io.reactivex.Flowable

/**
 * Created by tairo on 1/6/18 10:58 PM.
 */
class CompaniesLocalDataStore(private val companiesDao: CompaniesDao, private val jobsDao: JobsDao) :
    CompaniesLocalRepository {
    override fun add(company: Company): Long {
        return companiesDao.add(company)
    }

    override fun addAll(companies: List<Company>?) {
        companiesDao.addAll(companies)
    }

    override fun update(company: Company) {
        companiesDao.update(company)
    }

    override fun delete(company: Company) {
        companiesDao.delete(company)
    }

    override fun deleteAll() {
        companiesDao.deleteAll()
    }

    override fun getAll(): Flowable<List<Company>> {
        return companiesDao.getAll()
    }

    override fun getByID(id: Int): Flowable<Company> {
        return companiesDao.getByID(id)
    }

    override fun getAllByCompany(company: String): Flowable<List<Job>> {
        return jobsDao.getAllByCompany(company)
    }
}