defmodule SiteWeb.PageController do
  use SiteWeb, :controller

  def index(conn, _params) do
    render(conn, "index.html")
  end

  def about(conn, _params) do
    render(conn, "about.html")
  end

  def work(conn, _params) do
    render(conn, "work.html")
  end

  def os(conn, _params) do
    render(conn, "os.html")
  end

  def missing(conn, _params) do
    redirect(conn, to: "/")
  end
end
