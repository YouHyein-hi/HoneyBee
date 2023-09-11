package com.example.data.paging

//import android.util.Log
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.data.mapper.ResponseMapper.toServerBillData
//import com.example.data.remote.dataSource.GeneralDataSource
//import com.example.data.repoImpl.GeneralRepositoryImpl
//import com.example.domain.model.remote.receive.bill.BillData
//import com.example.domain.model.remote.receive.bill.ServerBillData
//import com.example.domain.util.StringUtil
//import javax.inject.Inject

/**
 * 2023-09-11
 * pureum
 */
//class BillListPageSource @Inject constructor(private val generalDataSource: GeneralDataSource):PagingSource<Int, BillData>() {
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BillData> {
//        return try {
//            val page = params.key?: 0
//            val results = generalDataSource.getBillListDataSource(page).toServerBillData()
//            val newList = results.body?.map { it.copy(date = StringUtil.changeDate(it.date), storeAmount = StringUtil.changeAmount(it.storeAmount)) }
//            val nextPage = if(results.body?.count() == 20) page + 1 else null
//            LoadResult.Page(data = newList!!.toMutableList(), nextKey = nextPage, prevKey = null)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, BillData>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//}