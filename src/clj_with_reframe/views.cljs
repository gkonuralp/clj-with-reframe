 (ns clj-with-reframe.views
   (:require
    [re-frame.core :as re-frame]
    [clj-with-reframe.events :as events]
    [clj-with-reframe.subs :as subs]))

(defn text-input [id label]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div.field
     [:label.label label]
     [:div.control
      [:input.input {:value @value
                     :on-change #(re-frame/dispatch [::events/update-form id (-> % .-target .-value)])
                     :type "text" :placeholder "Text input"}]]]))

(def animal-types ["Dog" "Cat" "Bird" "Fish" "Snake"])

(defn animal-list []
  (let [animals @(re-frame/subscribe [::subs/animals])]
    [:div
     [:h1.is-4.title.mb-2 "Animal List"]
     [:ul.mb-5
      (map (fn [{:keys [animal-type animal-name]}]
             [:li {:key animal-name} (str animal-name " (" animal-type ")")]) animals)]]))

(defn select-input [id label options]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div.field
     [:label.label label]
     [:div.control
      [:div.select
       [:select {:value @value :on-change #(re-frame/dispatch [::events/update-form id (-> % .-target .-value)])}
        [:option {:value ""} "Select an animal"]
        (map (fn [animal]
               [:option {:key animal :value animal} animal])
             options)]]]]))

(defn main-panel []
  (let [is-valid? @(re-frame/subscribe [::subs/form-is-valid? [:animal-name :animal-type]])]
    [:div.section
     [animal-list]
     [text-input :animal-name "Animal name"]
     [select-input :animal-type "Animal type" animal-types]
     [:button.button.is-primary {:disabled (not is-valid?) :on-click #(re-frame/dispatch [::events/save-form])} "Save"]]))

