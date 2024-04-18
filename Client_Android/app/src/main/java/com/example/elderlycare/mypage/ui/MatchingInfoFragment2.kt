package com.example.elderlycare.mypage.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.elderlycare.databinding.FragmentMatchingInfoBinding
import com.example.elderlycare.mypage.adapter.MatchingInfoAdapter
import com.example.elderlycare.mypage.adapter.MatchingInfoAdapter2
import com.example.elderlycare.mypage.service.CaregiverPageService
import com.example.elderlycare.mypage.service.SeniorPageService
import com.example.elderlycare.mypage.vo.MatchingDTO
import com.example.elderlycare.mypage.vo.MatchingResponse
import com.example.elderlycare.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [MatchingInfoFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class MatchingInfoFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentMatchingInfoBinding
    private lateinit var retrofit: Retrofit
    private lateinit var service: CaregiverPageService
    private lateinit var progressAdapter: MatchingInfoAdapter2
    private lateinit var pastAdapter: MatchingInfoAdapter2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_matching_info, container, false)
        binding = FragmentMatchingInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // <나의 정보 불러오기>
        setupRetrofit()
        // onViewCreated 메소드 내에 다음 코드 추가
        setupRecyclerViews()

        // SharedPreferences 에서
        val preferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = preferences.getString("user.email", "")
        val userId = preferences.getLong("user.userId", 0)
        Log.d(">>>>", "${userEmail}")
        Log.d(">>>>", "${userId}")

        getMatchingInfo(userId)


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MatchingInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MatchingInfoFragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }

    private fun setupRetrofit() {
        val client = setupOkHttpClient()

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL +"/m/caregiverPage/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        service = retrofit.create(CaregiverPageService::class.java)
    }

    private fun setupOkHttpClient(): OkHttpClient {
//        val cookieManager = CookieManager()
//        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
//        val cookieJar = JavaNetCookieJar(cookieManager)
//
        return OkHttpClient.Builder()
//            .cookieJar(cookieJar)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private fun getMatchingInfo(userId: Long) {
        service.matchingInfo(userId).enqueue(object : Callback<MatchingResponse> {
            override fun onResponse(call: Call<MatchingResponse>, response: Response<MatchingResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val matchingResponse = response.body()
                        if (matchingResponse != null) {
                            // 과거 매칭 리스트
                            val pastMatchings = matchingResponse.pastMatchings
                            val pastList: ArrayList<MatchingDTO> = ArrayList<MatchingDTO>(pastMatchings)
                            pastAdapter.setList(pastList)

                            Log.d(">>>", "Past Matchings: $pastMatchings")

                            // 진행 중인 매칭 리스트
                            val progressMatchings = matchingResponse.progressMatchings
                            val progressList: ArrayList<MatchingDTO> = ArrayList<MatchingDTO>(progressMatchings)
                            progressAdapter.setList(progressList)

                            Log.d(">>>", "Progress Matchings: $progressMatchings")
                        }
                    } else {
                        Log.e(">>", "Failed to fetch matching info")
                    }
                }
            }

            override fun onFailure(call: Call<MatchingResponse>, t: Throwable) {
                Log.e(">>", "Error: ${t.message}", t)
            }
        })
    }

    private fun setupRecyclerViews() {
        pastAdapter = MatchingInfoAdapter2()
        progressAdapter = MatchingInfoAdapter2()

        binding.pastMatching.layoutManager = LinearLayoutManager(requireContext())
        binding.pastMatching.adapter = pastAdapter

        binding.progMatching.layoutManager = LinearLayoutManager(requireContext())
        binding.progMatching.adapter = progressAdapter
    }
}