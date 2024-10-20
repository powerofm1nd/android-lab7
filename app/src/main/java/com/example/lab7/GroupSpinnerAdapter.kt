import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.lab7.data.Group

class GroupSpinnerAdapter(
    context: Context,
    groups: List<Group>
) : ArrayAdapter<Group>(context, android.R.layout.simple_spinner_item, groups) {

    init {
        // Устанавливаем layout для отображения выпадающего списка
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    // Этот метод отвечает за отображение текстового представления группы в Spinner
    override fun getItem(position: Int): Group? {
        return super.getItem(position)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        // Устанавливаем текст из поля number для каждой группы
        view.text = getItem(position)?.number
        return view
    }
}
