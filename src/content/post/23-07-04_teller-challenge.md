---
title: "Teller Bank Challenge"
description: "Writeup of my solution to the Teller Bank Challenge held during ElixirConf EU 2023."
publishDate: "23/07/04"
tags: ["elixir", "reverse engineering", "livebook"]
draft: true
---

Recently I participated in the Teller Bank Challenge held during ElixirConf EU 2023 and wanted to share my experience. You can find the challenge website [here](https://lisbon.teller.engineering/) and the code for my solution in Elixir [here](https://github.com/mvkvc/teller_bank_job). The main objective is to reverse engineer a fake banking API and write code to be able to view private account information.

On the challenge website, there is a simulator of the phone which you can use with passwords provided on the phone. Then you can see the network traffic between the banking app and the phone app through `telnet lisbon.teller.engineering 1337`. I went through the flow that I was trying to reverse engineer and copied all the network requests and responses into a file. You can see these is the [`/notes`](https://github.com/mvkvc/teller_bank_job/tree/main/notes) directory in the repo. Each function we build will be a step in the flow and we will use the responses from the previous step to build the next step.

<!-- For brevity I will only include the requests and responses that I used in my solution and anyone interested can look at the rest in the repo. -->

First let's install some dependencies. I used `req` to handle the HTTP requests and `jason` to serialize and deserialize to JSON.

```elixir
Mix.install([
  :req,
  :jason
])
```

The first request was:

```
GET /config
user-agent: Teller Bank iOS v1.3
api-key: Hello-Lisbon!
device-id: TU2CM7WPWZJVNK2N
accept: application/json
```

Let's recreate this request in Elixir:

```elixir
@url "https://lisbon.teller.engineering"
@user_agent "Teller Bank iOS v1.3"
@api_key "Hello-Lisbon!"
@device_id "TU2CM7WPWZJVNK2N"
@sms_code "001337"

def login({username, password}) do
    headers = %{
    user_agent: @user_agent,
    api_key: @api_key,
    device_id: @device_id,
    content_type: "application/json",
    accept: "application/json"
    }

    body =
    Jason.encode!(%{
        username: username,
        password: password
    })

    response = Req.post!("#{@url}/login", body: body, headers: headers)

    {response, username}
end
```

So far so good. The response was:

```
200 OK
teller-is-hiring: <https://jobs.lever.co/teller/dd21975a-901b-49ee-926c-336c92f8673d>
content-type: application/json; charset=utf-8
{
  "conference": [
    {
      "date": "2023-04-20",
      "schedule": [
        {
          "location": {
            "floor": "Floor -1",
            "room": "Europa",
            "track": "Track 1"
          },
          "speakers": [],
          "time": "9:00 AM – 9:20 AM",
          "title": "Welcome to the ElixirConf EU!"
        },
        ...
  ],
  "utils": {
    "arg_a": "555345524e414d45",
    "arg_b": "465f544f4b454e",
    "code": "1f8b08000000000000035d570b5c4c5b173fd399e695c969a69794462fa5f713514e254961f4c0478c69e65453f3726626532149e5f19508999f5eeabac9e37e2525ae1809e1de4f1ec95b12ca4d2ad7dbad6f4f4d57f73bbfdf9ab5f6daffb5f77a9cd9ebecf94b22dc2148ff5c6070c0a20079f44c0822744..."
  }
}
```

We recieve a long list of all the conference talks and then more importantly this `utils` object. It contains three arguments which all seem encoded in some way. Let's continue and we will come back to these later as needed.

The next request was:

```
POST /login
user-agent: Teller Bank iOS v1.3
api-key: Hello-Lisbon!
device-id: TU2CM7WPWZJVNK2N
content-type: application/json
accept: application/json
{
  "password": "el1x1r_c0nf_Lisbon",
  "username": "elixir"
}
```

Let's recreate this request in Elixir with our account credentials:

```elixir
def request_mfa({response, username}) do
    request_token = get_header_val(response.headers, "request-token")
    last_request_id = get_header_val(response.headers, "f-request-id")
    f_token_spec = get_header_val(response.headers, "f-token-spec")

    f_token = gen_f_token(f_token_spec, last_request_id, username)

    sms_id =
    response.body
    |> Map.get("devices")
    |> Enum.find(&(&1["type"] == "SMS"))
    |> Map.get("id")

    headers = %{
    teller_is_hiring: "I know!",
    user_agent: @user_agent,
    api_key: @api_key,
    device_id: @device_id,
    request_token: request_token,
    f_token: f_token,
    content_type: "application/json",
    accept: "application/json"
    }

    body = %{device_id: sms_id} |> Jason.encode!()

    response = Req.post!("#{@url}/login/mfa/request", body: body, headers: headers)

    {response, username}
end
```

The response was:

```elixir
{%Req.Response{
   status: 200,
   headers: [
     {"cache-control", "max-age=0, private, must-revalidate"},
     {"content-encoding", "gzip"},
     {"content-type", "application/json; charset=utf-8"},
     {"date", "Mon, 12 Jun 2023 00:26:19 GMT"},
     {"f-request-id", "req_eecju2gzawx2fyv5cnckhgjiuaenpi3v5tgkdra"},
     {"f-token-spec",
      "eyJtZXRob2QiOiJoc3otZ2RsLXVyZXYtaHJjLXl6aHYtZ3NyaWdiLWdkbC1vbGR2aS14emh2LW1sLWt6d3dybXR0Iiwic2VwYXJhdG9yIjoiJCIsInZhbHVlcyI6WyJhcGkta2V5IiwibGFzdC1yZXF1ZXN0LWlkIiwidXNlcm5hbWUiLCJkZXZpY2UtaWQiXX0"},
     {"request-token",
      "QTEyOEdDTQ.n_nYz3NxF2U-hxZ3nHMeWUje-0w9hvu7_CG2dGbddfIIyRP1XyZfG9uLwe4.faOA6APZnsP7NcDd.-tpSM6zPTdSFlDpMRZ9HjwnuWVk3E7fr2tvw9J80cJAghtFo9Bdju5ELPN-B78sUllJUgFIfdV9PAGysxdh_wUo_W4TSugczKr8Qn___vFlYVl24jxO7ZMSvfhnF0dW7nGYoknCeWEvPMfAp53_4EATKOtrGGJMj7CATxN8T3-hOVlLNVA.juSwOD0LRAtNhn5V_PGXBQ"},
     {"server", "Fly/ece29468b (2023-06-07)"},
     {"teller-is-hiring", "https://jobs.lever.co/teller/dd21975a-901b-49ee-926c-336c92f8673d"},
     {"timestamp", "1686529579459"},
     {"x-request-id", "F2fBt4a4iFabiyYAAHkh"},
     {"transfer-encoding", "chunked"},
     {"via", "1.1 fly.io"},
     {"fly-request-id", "01H2PFY6C973HTFPS5AJ5VTWZQ-yyz"}
   ],
   body: %{
     "devices" => [
       %{
         "id" => "voice_ad_yhjr3ruiye3n7t5iz5qgrr3f7elt3xxfgkpjrxq",
         "mask" => "***7095",
         "type" => "VOICE"
       },
       %{
         "id" => "sms_ad_yhjr3ruiye3n7t5iz5qgrr3f7elt3xxfgkpjrxq",
         "mask" => "***7095",
         "type" => "SMS"
       }
     ],
     "mfa_required" => true
   },
   private: %{}
 }, "blue_chloe"}
```

There are some interesting values in the response headers and the body. Let's keep going and see what we need.
