(ns meuse.front.base
  (:require [hiccup.element :refer [link-to]]
            [hiccup.page :as page]))

(def head
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1, shrink-to-fit=no"}]
   [:title "Meuse"]
   [:meta {:name "description"
           ;; todo: replace this with the crate's description on crate pages
           :content "A private Rust crate registry"}]
   (page/include-css "/static/css/bootstrap.min.css")
   (page/include-css "/static/css/style.css")])

(defn menu
  [public-frontend config]
  [:div {:id "menu"}
   [:div {:class "row"}

    (if-let [title (:title config)]
      (do (link-to {:id "title"} "/front" title))
      (do (link-to {:id "title"} "/front" "Meuse")))

    [:div {:id "menu-search"}
     [:form {:action "/front/search" :method "get"}
      [:input {:type "search"
               :id "menu-search-input"
               :name "q"
               :placeholder "Search"
               :alt "Search"}]]]

    [:div {:id "menu-links"}
     [:span {:class "menu-element"} [:a {:href "/front/crates"} "All Crates"]]
     [:span {:class "menu-element"} "·"]
     [:span {:class "menu-element"} [:a {:href "/front/categories"} "Categories"]]
     (when-not public-frontend
       [:span
        [:span {:class "menu-element"} "·"]
        [:span {:class "menu-element"} [:a {:href "/front/tokens"} "Tokens"]]
        [:span {:class "menu-element"} "·"]
        [:span {:class "menu-element"}
         [:form {:id "logout"
                 :action "/front/logout"
                 :method "post"}
          [:a {:href "javascript:{}"
               :onclick "document.getElementById('logout').submit(); return false;"}
           "Logout"]]]])]]])

(defn footer
  [config]
  [:footer {:class "container"}
   [:p
    (when-let [documentation (:documentation config)]
      [:a {:href documentation} "Documentation"])
    " "
    (when-let [repository (:repository config)]
      [:a {:href repository} "Repository"])]])

(defn html
  [body public-frontend config]
  (page/html5
   {:lang "en"}
   head
   [:body
    [:div {:id "content"}
     (menu public-frontend config)
     [:div {:id "core"} body]
     (footer config)]
    (page/include-js "/static/js/jquery-3.4.1.slim.min.js")
    (page/include-js "/static/js/popper.min.js")
    (page/include-js "/static/js/bootstrap.min.js")]))
