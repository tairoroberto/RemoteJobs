package com.remoteok.io.app.data.companies

import com.remoteok.io.app.data.dao.CompaniesDao
import com.remoteok.io.app.domain.companies.CompaniesLocalRepository
import com.remoteok.io.app.model.Company
import io.reactivex.Flowable

/**
 * Created by tairo on 1/6/18 10:58 PM.
 */
class CompaniesLocalDataStore(private val companiesDao: CompaniesDao) : CompaniesLocalRepository {
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
}