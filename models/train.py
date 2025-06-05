import sys
import os
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, classification_report
import joblib

def load_dataset(dataset_path_or_id):
    if os.path.isfile(dataset_path_or_id):
        csv_path = dataset_path_or_id
    else:
        base_dir = os.path.dirname(os.path.abspath(__file__))
        csv_path = os.path.join(base_dir, f"dataset_{dataset_path_or_id}.csv")

    if not os.path.exists(csv_path):
        raise FileNotFoundError(f"[ERREUR] Fichier introuvable : {csv_path}")
    
    return pd.read_csv(csv_path, encoding="utf-8")

def preprocess_data(df):
    expected_columns = {'text1', 'text2', 'classe'}
    
    if not expected_columns.issubset(df.columns):
        raise ValueError(f"[ERREUR] Le CSV doit contenir les colonnes : {expected_columns}")

    df = df.dropna(subset=['text1', 'text2', 'classe'])  # Supprimer les lignes avec valeurs nulles
    
    df['text1'] = df['text1'].astype(str).str.lower().str.replace(r"[^a-zA-Z0-9\s]", "", regex=True)
    df['text2'] = df['text2'].astype(str).str.lower().str.replace(r"[^a-zA-Z0-9\s]", "", regex=True)
    
    texts = df['text1'] + " " + df['text2']
    return texts, df['classe']

def train_model(X, y):
    vectorizer = TfidfVectorizer()
    X_vec = vectorizer.fit_transform(X)

    X_train, X_test, y_train, y_test = train_test_split(X_vec, y, test_size=0.2, random_state=42)

    model = RandomForestClassifier(random_state=42)
    model.fit(X_train, y_train)

    y_pred = model.predict(X_test)

    acc = accuracy_score(y_test, y_pred)
    report = classification_report(y_test, y_pred)

    return model, vectorizer, acc, report, X_test, y_test

def save_model(model, vectorizer, dataset_id, X_test, y_test):
    model_dir = "models"
    os.makedirs(model_dir, exist_ok=True)

    base_name = os.path.basename(str(dataset_id))
    clean_name = os.path.splitext(base_name)[0]

    model_path = os.path.join(model_dir, f"model_{clean_name}.pkl")
    vectorizer_path = os.path.join(model_dir, f"vectorizer_{clean_name}.pkl")
    testdata_path = os.path.join(model_dir, f"testdata_{clean_name}.pkl")

    joblib.dump(model, model_path)
    joblib.dump(vectorizer, vectorizer_path)
    joblib.dump((X_test, y_test), testdata_path)

    print(f"[INFO] Modèle sauvegardé : {model_path}")
    print(f"[INFO] Vectorizer sauvegardé : {vectorizer_path}")
    print(f"[INFO] Données de test sauvegardées : {testdata_path}")

def main():
    if len(sys.argv) < 2:
        print("[ERREUR] Veuillez spécifier un dataset_id ou chemin vers CSV.")
        sys.exit(1)

    dataset_id = sys.argv[1]

    try:
        print(f"[INFO] Début de l'entraînement pour le dataset {dataset_id}...")
        df = load_dataset(dataset_id)
        print(f"[INFO] Chargement du fichier CSV réussi ({len(df)} lignes)")

        X, y = preprocess_data(df)
        print(f"[INFO] Prétraitement terminé - {len(X)} échantillons prêts")

        model, vectorizer, acc, report, X_test, y_test = train_model(X, y)
        print("[INFO] Modèle entraîné avec succès !")

        save_model(model, vectorizer, dataset_id, X_test, y_test)

        print(f"\n[INFO] Entraînement terminé pour dataset {dataset_id}")
        print(f"[RESULTAT] Accuracy: {acc:.4f}\n")
        print("[INFO] Rapport de classification :")
        print(report)

    except Exception as e:
        print(f"[ERREUR] Une exception est survenue : {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()
